package org.dokiteam.doki.settings.tracker.categories

import org.dokiteam.doki.core.model.FavouriteCategory
import org.dokiteam.doki.core.ui.BaseListAdapter
import org.dokiteam.doki.core.ui.list.OnListItemClickListener

class TrackerCategoriesConfigAdapter(
	listener: OnListItemClickListener<FavouriteCategory>,
) : BaseListAdapter<FavouriteCategory>() {

	init {
		delegatesManager.addDelegate(trackerCategoryAD(listener))
	}
}
