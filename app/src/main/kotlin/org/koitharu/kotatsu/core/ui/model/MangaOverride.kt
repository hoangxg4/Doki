package org.dokiteam.doki.core.ui.model

import org.dokiteam.doki.parsers.model.ContentRating

data class MangaOverride(
	val coverUrl: String?,
	val title: String?,
	val contentRating: ContentRating?,
)
