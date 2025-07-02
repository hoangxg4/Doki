package org.dokiteam.doki.core.exceptions

import okio.IOException
import org.dokiteam.doki.parsers.model.MangaSource

abstract class CloudFlareException(
	message: String,
	val state: Int,
) : IOException(message) {

	abstract val url: String

	abstract val source: MangaSource
}
