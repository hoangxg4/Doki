package org.dokiteam.doki.tracker.domain.model

import org.dokiteam.doki.parsers.model.Manga
import java.time.Instant

data class TrackingLogItem(
	val id: Long,
	val manga: Manga,
	val chapters: List<String>,
	val createdAt: Instant,
	val isNew: Boolean,
)
