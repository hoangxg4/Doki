package org.dokiteam.doki.explore.ui.model

import org.dokiteam.doki.core.model.MangaSourceInfo
import org.dokiteam.doki.list.ui.model.ListModel
import org.dokiteam.doki.parsers.util.longHashCode

data class MangaSourceItem(
	val source: MangaSourceInfo,
	val isGrid: Boolean,
) : ListModel {

	val id: Long = source.name.longHashCode()

	override fun areItemsTheSame(other: ListModel): Boolean {
		return other is MangaSourceItem && other.source == source
	}
}
