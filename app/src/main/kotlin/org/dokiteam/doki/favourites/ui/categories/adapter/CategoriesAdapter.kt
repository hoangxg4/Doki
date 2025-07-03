package org.dokiteam.doki.favourites.ui.categories.adapter

import org.dokiteam.doki.core.ui.ReorderableListAdapter
import org.dokiteam.doki.favourites.ui.categories.FavouriteCategoriesListListener
import org.dokiteam.doki.list.ui.adapter.ListItemType
import org.dokiteam.doki.list.ui.adapter.ListStateHolderListener
import org.dokiteam.doki.list.ui.adapter.emptyStateListAD
import org.dokiteam.doki.list.ui.adapter.loadingStateAD
import org.dokiteam.doki.list.ui.model.ListModel

class CategoriesAdapter(
	onItemClickListener: FavouriteCategoriesListListener,
	listListener: ListStateHolderListener,
) : ReorderableListAdapter<ListModel>() {

	init {
		addDelegate(ListItemType.CATEGORY_LARGE, categoryAD(onItemClickListener))
		addDelegate(ListItemType.NAV_ITEM, allCategoriesAD(onItemClickListener))
		addDelegate(ListItemType.STATE_EMPTY, emptyStateListAD(listListener))
		addDelegate(ListItemType.STATE_LOADING, loadingStateAD())
	}
}
