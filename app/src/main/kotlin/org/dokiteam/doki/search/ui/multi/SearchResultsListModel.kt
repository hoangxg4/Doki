package org.dokiteam.doki.search.ui.multi

import android.content.Context
import androidx.annotation.StringRes
import org.dokiteam.doki.core.model.getTitle
import org.dokiteam.doki.list.ui.ListModelDiffCallback
import org.dokiteam.doki.list.ui.model.ListModel
import org.dokiteam.doki.list.ui.model.MangaListModel
import org.dokiteam.doki.parsers.model.MangaListFilter
import org.dokiteam.doki.parsers.model.MangaSource
import org.dokiteam.doki.parsers.model.SortOrder

data class SearchResultsListModel(
	@StringRes val titleResId: Int,
	val source: MangaSource,
	val listFilter: MangaListFilter?,
	val sortOrder: SortOrder?,
	val list: List<MangaListModel>,
	val error: Throwable?,
) : ListModel {

	fun getTitle(context: Context): String = if (titleResId != 0) {
		context.getString(titleResId)
	} else {
		source.getTitle(context)
	}

	override fun areItemsTheSame(other: ListModel): Boolean {
		return other is SearchResultsListModel && source == other.source && titleResId == other.titleResId
	}

	override fun getChangePayload(previousState: ListModel): Any? {
		return if (previousState is SearchResultsListModel && previousState.list != list) {
			ListModelDiffCallback.PAYLOAD_NESTED_LIST_CHANGED
		} else {
			super.getChangePayload(previousState)
		}
	}
}
