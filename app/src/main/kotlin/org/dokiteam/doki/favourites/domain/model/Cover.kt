package org.dokiteam.doki.favourites.domain.model

import org.dokiteam.doki.core.model.MangaSource

data class Cover(
	val url: String?,
	val source: String,
) {
	val mangaSource by lazy { MangaSource(source) }
}
