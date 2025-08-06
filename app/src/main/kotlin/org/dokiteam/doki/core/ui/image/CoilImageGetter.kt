package org.dokiteam.doki.core.ui.image

import android.content.Context
import android.graphics.drawable.Drawable
import android.text.Html
import androidx.annotation.WorkerThread
import coil3.ImageLoader
import coil3.executeBlocking
import coil3.request.ImageRequest
import coil3.request.allowHardware
import coil3.request.crossfade
import dagger.hilt.android.qualifiers.ApplicationContext
import org.dokiteam.doki.core.util.ext.drawable
import javax.inject.Inject

class CoilImageGetter @Inject constructor(
	@ApplicationContext private val context: Context,
	private val coil: ImageLoader,
) : Html.ImageGetter {

	@WorkerThread
	override fun getDrawable(source: String?): Drawable? {
		return coil.executeBlocking(
			ImageRequest.Builder(context)
				.data(source)
				.allowHardware(false)
				.crossfade(true)
				.build(),
		).drawable?.apply {
			setBounds(0, 0, intrinsicHeight, intrinsicHeight)
		}
	}
}
