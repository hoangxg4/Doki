package org.dokiteam.doki.core.parser

import org.dokiteam.doki.core.exceptions.UnsupportedSourceException
import org.dokiteam.doki.parsers.MangaLoaderContext
import org.dokiteam.doki.parsers.config.ConfigKey
import org.dokiteam.doki.parsers.core.AbstractMangaParser
import org.dokiteam.doki.parsers.model.Manga
import org.dokiteam.doki.parsers.model.MangaChapter
import org.dokiteam.doki.parsers.model.MangaListFilterOptions
import org.dokiteam.doki.parsers.model.MangaPage
import org.dokiteam.doki.parsers.model.MangaParserSource
import org.dokiteam.doki.parsers.model.SortOrder
import org.dokiteam.doki.parsers.model.search.MangaSearchQuery
import org.dokiteam.doki.parsers.model.search.MangaSearchQueryCapabilities
import java.util.EnumSet

/**
 * This parser is just for parser development, it should not be used in releases
 */
class DummyParser(context: MangaLoaderContext) : AbstractMangaParser(context, MangaParserSource.DUMMY) {

	override val configKeyDomain: ConfigKey.Domain
		get() = ConfigKey.Domain("localhost")

	override val availableSortOrders: Set<SortOrder>
		get() = EnumSet.allOf(SortOrder::class.java)

	override val searchQueryCapabilities: MangaSearchQueryCapabilities
		get() = MangaSearchQueryCapabilities()

	override suspend fun getDetails(manga: Manga): Manga = stub(manga)

	override suspend fun getFilterOptions(): MangaListFilterOptions = stub(null)

	override suspend fun getList(query: MangaSearchQuery): List<Manga> = stub(null)

	override suspend fun getPages(chapter: MangaChapter): List<MangaPage> = stub(null)

	private fun stub(manga: Manga?): Nothing {
		throw UnsupportedSourceException("Usage of Dummy parser", manga)
	}
}
