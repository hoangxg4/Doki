package org.dokiteam.doki.download.ui.list

import androidx.lifecycle.LifecycleOwner
import org.dokiteam.doki.core.ui.BaseListAdapter
import org.dokiteam.doki.list.ui.adapter.ListItemType
import org.dokiteam.doki.list.ui.adapter.emptyStateListAD
import org.dokiteam.doki.list.ui.adapter.listHeaderAD
import org.dokiteam.doki.list.ui.adapter.loadingStateAD
import org.dokiteam.doki.list.ui.model.ListModel

class DownloadsAdapter(
	lifecycleOwner: LifecycleOwner,
	listener: DownloadItemListener,
) : BaseListAdapter<ListModel>() {

	init {
		addDelegate(ListItemType.DOWNLOAD, downloadItemAD(lifecycleOwner, listener))
		addDelegate(ListItemType.STATE_LOADING, loadingStateAD())
		addDelegate(ListItemType.STATE_EMPTY, emptyStateListAD(null))
		addDelegate(ListItemType.HEADER, listHeaderAD(null))
	}
}
