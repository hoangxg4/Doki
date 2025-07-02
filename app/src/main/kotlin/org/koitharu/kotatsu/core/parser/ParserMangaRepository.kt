package org.dokiteam.doki.core.parser

import kotlinx.coroutines.Dispatchers
import okhttp3.Interceptor
import okhttp3.Response
import org.dokiteam.doki.core.cache.MemoryContentCache
import org.dokiteam.doki.core.network.MirrorSwitchInterceptor
import org.dokiteam.doki.core.prefs.SourceSettings
import org.dokiteam.doki.parsers.MangaParser
import org.dokiteam.doki.parsers.MangaParserAuthProvider
import org.dokiteam.doki.parsers.config.ConfigKey
import org.dokiteam.doki.parsers.model.Favicons
import org.dokiteam.doki.parsers.model.Manga
import org.dokiteam.doki.parsers.model.MangaChapter
import org.dokiteam.doki.parsers.model.MangaListFilter
import org.dokiteam.doki.parsers.model.MangaListFilterCapabilities
import org.dokiteam.doki.parsers.model.MangaListFilterOptions
import org.dokiteam.doki.parsers.model.MangaPage
import org.dokiteam.doki.parsers.model.MangaParserSource
import org.dokiteam.doki.parsers.model.SortOrder
import org.dokiteam.doki.parsers.util.runCatchingCancellable
import org.dokiteam.doki.parsers.util.suspendlazy.suspendLazy

class ParserMangaRepository(
	private val parser: MangaParser,
	private val mirrorSwitchInterceptor: MirrorSwitchInterceptor,
	cache: MemoryContentCache,
) : CachingMangaRepository(cache), Interceptor {

	private val filterOptionsLazy = suspendLazy(Dispatchers.Default) {
		mirrorSwitchInterceptor.withMirrorSwitching {
			parser.getFilterOptions()
		}
	}

	override val source: MangaParserSource
		get() = parser.source

	override val sortOrders: Set<SortOrder>
		get() = parser.availableSortOrders

	override val filterCapabilities: MangaListFilterCapabilities
		get() = parser.filterCapabilities

	override var defaultSortOrder: SortOrder
		get() = getConfig().defaultSortOrder ?: sortOrders.first()
		set(value) {
			getConfig().defaultSortOrder = value
		}

	var domain: String
		get() = parser.domain
		set(value) {
			getConfig()[parser.configKeyDomain] = value
		}

	val domains: Array<out String>
		get() = parser.configKeyDomain.presetValues

	override fun intercept(chain: Interceptor.Chain): Response = parser.intercept(chain)

	override suspend fun getList(offset: Int, order: SortOrder?, filter: MangaListFilter?): List<Manga> {
		return mirrorSwitchInterceptor.withMirrorSwitching {
			parser.getList(offset, order ?: defaultSortOrder, filter ?: MangaListFilter.EMPTY)
		}
	}

	override suspend fun getPagesImpl(
		chapter: MangaChapter
	): List<MangaPage> = mirrorSwitchInterceptor.withMirrorSwitching {
		parser.getPages(chapter)
	}

	override suspend fun getPageUrl(page: MangaPage): String = mirrorSwitchInterceptor.withMirrorSwitching {
		parser.getPageUrl(page).also { result ->
			check(result.isNotEmpty()) { "Page url is empty" }
		}
	}

	override suspend fun getFilterOptions(): MangaListFilterOptions = filterOptionsLazy.get()

	suspend fun getFavicons(): Favicons = mirrorSwitchInterceptor.withMirrorSwitching {
		parser.getFavicons()
	}

	override suspend fun getRelatedMangaImpl(seed: Manga): List<Manga> = parser.getRelatedManga(seed)

	override suspend fun getDetailsImpl(manga: Manga): Manga = mirrorSwitchInterceptor.withMirrorSwitching {
		parser.getDetails(manga)
	}

	fun getAuthProvider(): MangaParserAuthProvider? = parser.authorizationProvider

	fun getRequestHeaders() = parser.getRequestHeaders()

	fun getConfigKeys(): List<ConfigKey<*>> = ArrayList<ConfigKey<*>>().also {
		parser.onCreateConfig(it)
	}

	fun getAvailableMirrors(): List<String> {
		return parser.configKeyDomain.presetValues.toList()
	}

	fun isSlowdownEnabled(): Boolean {
		return getConfig().isSlowdownEnabled
	}

	fun getConfig() = parser.config as SourceSettings

	private suspend fun <R> MirrorSwitchInterceptor.withMirrorSwitching(block: suspend () -> R): R {
		if (!isEnabled) {
			return block()
		}
		val initialMirror = domain
		val result = runCatchingCancellable {
			block()
		}
		if (result.isValidResult()) {
			return result.getOrThrow()
		}
		return if (trySwitchMirror(this@ParserMangaRepository)) {
			val newResult = runCatchingCancellable {
				block()
			}
			if (newResult.isValidResult()) {
				return newResult.getOrThrow()
			} else {
				rollback(this@ParserMangaRepository, initialMirror)
				return result.getOrThrow()
			}
		} else {
			result.getOrThrow()
		}
	}

	private fun Result<*>.isValidResult() = isSuccess && (getOrNull() as? Collection<*>)?.isEmpty() != true
}
