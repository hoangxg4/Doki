package org.dokiteam.doki.image.ui

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import androidx.core.graphics.drawable.toBitmap
import androidx.lifecycle.SavedStateHandle
import coil3.ImageLoader
import coil3.request.CachePolicy
import coil3.request.ImageRequest
import coil3.request.allowHardware
import coil3.request.crossfade
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runInterruptible
import org.dokiteam.doki.core.model.MangaSource
import org.dokiteam.doki.core.nav.AppRouter
import org.dokiteam.doki.core.ui.BaseViewModel
import org.dokiteam.doki.core.util.ext.MutableEventFlow
import org.dokiteam.doki.core.util.ext.call
import org.dokiteam.doki.core.util.ext.getDrawableOrThrow
import org.dokiteam.doki.core.util.ext.mangaSourceExtra
import org.dokiteam.doki.core.util.ext.require
import javax.inject.Inject

@HiltViewModel
class ImageViewModel @Inject constructor(
	@ApplicationContext private val context: Context,
	private val savedStateHandle: SavedStateHandle,
	private val coil: ImageLoader,
) : BaseViewModel() {

	val onImageSaved = MutableEventFlow<Uri>()

	fun saveImage(destination: Uri) {
		launchLoadingJob(Dispatchers.Default) {
			val request = ImageRequest.Builder(context)
				.allowHardware(false)
				.crossfade(true)
				.memoryCachePolicy(CachePolicy.READ_ONLY)
				.data(savedStateHandle.require<Uri>(AppRouter.KEY_DATA))
				.memoryCachePolicy(CachePolicy.DISABLED)
				.mangaSourceExtra(MangaSource(savedStateHandle[AppRouter.KEY_SOURCE]))
				.build()
			val bitmap = coil.execute(request).getDrawableOrThrow().toBitmap()
			runInterruptible(Dispatchers.IO) {
				context.contentResolver.openOutputStream(destination)?.use { output ->
					check(bitmap.compress(Bitmap.CompressFormat.PNG, 100, output))
				} ?: error("Cannot open output stream")
			}
			onImageSaved.call(destination)
		}
	}
}
