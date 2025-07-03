package org.dokiteam.doki.favourites.ui.categories.select.adapter

import org.dokiteam.doki.core.ui.BaseListAdapter
import org.dokiteam.doki.core.ui.list.OnListItemClickListener
import org.dokiteam.doki.favourites.ui.categories.select.model.MangaCategoryItem
import org.dokiteam.doki.list.ui.adapter.ListItemType
import org.dokiteam.doki.list.ui.adapter.emptyStateListAD
import org.dokiteam.doki.list.ui.adapter.loadingStateAD
import org.dokiteam.doki.list.ui.model.ListModel

class MangaCategoriesAdapter(
	clickListener: OnListItemClickListener<MangaCategoryItem>,
) : BaseListAdapter<ListModel>() {

	init {
		addDelegate(ListItemType.NAV_ITEM, mangaCategoryAD(clickListener))
		addDelegate(ListItemType.STATE_LOADING, loadingStateAD())
		addDelegate(ListItemType.STATE_EMPTY, emptyStateListAD(null))
	}
}
