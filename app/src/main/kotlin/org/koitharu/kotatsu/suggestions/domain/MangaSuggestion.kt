package org.dokiteam.doki.suggestions.domain

import androidx.annotation.FloatRange
import org.dokiteam.doki.parsers.model.Manga

data class MangaSuggestion(
	val manga: Manga,
	@FloatRange(from = 0.0, to = 1.0)
	val relevance: Float,
)