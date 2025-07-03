package org.dokiteam.doki.details.ui.scrobbling

import org.dokiteam.doki.core.nav.AppRouter
import org.dokiteam.doki.core.ui.BaseListAdapter
import org.dokiteam.doki.list.ui.model.ListModel

class ScrollingInfoAdapter(
	router: AppRouter,
) : BaseListAdapter<ListModel>() {

	init {
		delegatesManager.addDelegate(scrobblingInfoAD(router))
	}
}
