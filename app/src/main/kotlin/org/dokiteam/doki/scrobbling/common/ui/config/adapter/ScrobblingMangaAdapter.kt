package org.dokiteam.doki.scrobbling.common.ui.config.adapter

import org.dokiteam.doki.core.ui.BaseListAdapter
import org.dokiteam.doki.core.ui.list.OnListItemClickListener
import org.dokiteam.doki.list.ui.adapter.ListItemType
import org.dokiteam.doki.list.ui.adapter.emptyStateListAD
import org.dokiteam.doki.list.ui.model.ListModel
import org.dokiteam.doki.scrobbling.common.domain.model.ScrobblingInfo

class ScrobblingMangaAdapter(
	clickListener: OnListItemClickListener<ScrobblingInfo>,
) : BaseListAdapter<ListModel>() {

	init {
		addDelegate(ListItemType.HEADER, scrobblingHeaderAD())
		addDelegate(ListItemType.STATE_EMPTY, emptyStateListAD(null))
		addDelegate(ListItemType.MANGA_SCROBBLING, scrobblingMangaAD(clickListener))
	}
}
