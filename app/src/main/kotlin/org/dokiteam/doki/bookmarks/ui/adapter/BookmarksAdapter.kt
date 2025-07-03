package org.dokiteam.doki.bookmarks.ui.adapter

import android.content.Context
import org.dokiteam.doki.bookmarks.domain.Bookmark
import org.dokiteam.doki.core.ui.BaseListAdapter
import org.dokiteam.doki.core.ui.list.OnListItemClickListener
import org.dokiteam.doki.core.ui.list.fastscroll.FastScroller
import org.dokiteam.doki.list.ui.adapter.ListHeaderClickListener
import org.dokiteam.doki.list.ui.adapter.ListItemType
import org.dokiteam.doki.list.ui.adapter.emptyStateListAD
import org.dokiteam.doki.list.ui.adapter.errorStateListAD
import org.dokiteam.doki.list.ui.adapter.listHeaderAD
import org.dokiteam.doki.list.ui.adapter.loadingFooterAD
import org.dokiteam.doki.list.ui.adapter.loadingStateAD
import org.dokiteam.doki.list.ui.model.ListModel

class BookmarksAdapter(
	clickListener: OnListItemClickListener<Bookmark>,
	headerClickListener: ListHeaderClickListener?,
) : BaseListAdapter<ListModel>(), FastScroller.SectionIndexer {

	init {
		addDelegate(ListItemType.PAGE_THUMB, bookmarkLargeAD(clickListener))
		addDelegate(ListItemType.HEADER, listHeaderAD(headerClickListener))
		addDelegate(ListItemType.STATE_ERROR, errorStateListAD(null))
		addDelegate(ListItemType.FOOTER_LOADING, loadingFooterAD())
		addDelegate(ListItemType.STATE_LOADING, loadingStateAD())
		addDelegate(ListItemType.STATE_EMPTY, emptyStateListAD(null))
	}

	override fun getSectionText(context: Context, position: Int): CharSequence? {
		return findHeader(position)?.getText(context)
	}
}
