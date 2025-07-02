package org.dokiteam.doki.details.ui.pager.pages

import org.dokiteam.doki.list.ui.model.ListModel
import org.dokiteam.doki.reader.ui.pager.ReaderPage

data class PageThumbnail(
	val isCurrent: Boolean,
	val page: ReaderPage,
) : ListModel {

	val number
		get() = page.index + 1

	override fun areItemsTheSame(other: ListModel): Boolean {
		return other is PageThumbnail && page == other.page
	}
}
