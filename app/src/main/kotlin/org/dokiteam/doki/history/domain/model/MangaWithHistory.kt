package org.dokiteam.doki.history.domain.model

import org.dokiteam.doki.core.model.MangaHistory
import org.dokiteam.doki.parsers.model.Manga

data class MangaWithHistory(
	val manga: Manga,
	val history: MangaHistory
)
