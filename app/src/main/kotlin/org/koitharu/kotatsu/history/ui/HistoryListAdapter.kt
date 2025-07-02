package org.dokiteam.doki.history.ui

import android.content.Context
import org.dokiteam.doki.core.ui.list.fastscroll.FastScroller
import org.dokiteam.doki.list.ui.adapter.MangaListAdapter
import org.dokiteam.doki.list.ui.adapter.MangaListListener
import org.dokiteam.doki.list.ui.size.ItemSizeResolver

class HistoryListAdapter(
	listener: MangaListListener,
	sizeResolver: ItemSizeResolver,
) : MangaListAdapter(listener, sizeResolver), FastScroller.SectionIndexer {

	override fun getSectionText(context: Context, position: Int): CharSequence? {
		return findHeader(position)?.getText(context)
	}
}
