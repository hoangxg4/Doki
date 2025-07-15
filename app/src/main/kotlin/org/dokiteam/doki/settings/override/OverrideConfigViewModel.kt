package org.dokiteam.doki.settings.override

import android.content.Context
import androidx.core.net.toUri
import androidx.lifecycle.SavedStateHandle
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.withContext
import okio.buffer
import okio.sink
import org.dokiteam.doki.core.model.parcelable.ParcelableManga
import org.dokiteam.doki.core.nav.AppRouter
import org.dokiteam.doki.core.parser.MangaDataRepository
import org.dokiteam.doki.core.ui.BaseViewModel
import org.dokiteam.doki.core.ui.model.MangaOverride
import org.dokiteam.doki.core.util.MimeTypes
import org.dokiteam.doki.core.util.ext.MutableEventFlow
import org.dokiteam.doki.core.util.ext.call
import org.dokiteam.doki.core.util.ext.isFileUri
import org.dokiteam.doki.core.util.ext.openSource
import org.dokiteam.doki.core.util.ext.require
import org.dokiteam.doki.core.util.ext.toMimeTypeOrNull
import org.dokiteam.doki.core.util.ext.toUriOrNull
import org.dokiteam.doki.parsers.model.Manga
import org.dokiteam.doki.parsers.util.md5
import java.io.File
import javax.inject.Inject

private const val DIR_COVERS = "covers"

@HiltViewModel
class OverrideConfigViewModel @Inject constructor(
	savedStateHandle: SavedStateHandle,
	@ApplicationContext private val context: Context,
	private val dataRepository: MangaDataRepository,
) : BaseViewModel() {

	private val manga = savedStateHandle.require<ParcelableManga>(AppRouter.KEY_MANGA).manga

	val data = MutableStateFlow<Pair<Manga, MangaOverride>?>(null)
	val onSaved = MutableEventFlow<Unit>()

	init {
		launchLoadingJob(Dispatchers.Default) {
			data.value = manga to (dataRepository.getOverride(manga.id) ?: emptyOverride())
		}
	}

	fun save(title: String?) {
		launchLoadingJob(Dispatchers.Default) {
			val override = checkNotNull(data.value).second.let {
				it.copy(
					title = title,
					coverUrl = it.coverUrl?.cachedFile(),
				)
			}
			dataRepository.setOverride(manga, override)
			onSaved.call(Unit)
		}
	}

	fun updateCover(coverUri: String?) {
		val snapshot = data.value ?: return
		data.value = snapshot.first to snapshot.second.copy(
			coverUrl = coverUri,
		)
	}

	private suspend fun String.cachedFile(): String {
		val uri = toUriOrNull()
		if (uri == null || uri.isFileUri()) {
			return this
		}
		val cacheDir = context.getExternalFilesDir(DIR_COVERS) ?: return this
		val cr = context.contentResolver
		val ext = cr.getType(uri)?.toMimeTypeOrNull()?.let {
			MimeTypes.getExtension(it)
		}
		val fileName = buildString {
			append(this@cachedFile.md5())
			if (!ext.isNullOrEmpty()) {
				append('.')
				append(ext)
			}
		}
		return withContext(Dispatchers.IO) {
			val dest = File(cacheDir, fileName)
			cr.openSource(uri).use { source ->
				dest.sink().buffer().use { sink ->
					sink.writeAll(source)
				}
			}
			dest
		}.toUri().toString()
	}

	private fun emptyOverride() = MangaOverride(null, null, null)
}
