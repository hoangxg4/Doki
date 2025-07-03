package org.dokiteam.doki.list.ui.adapter

import android.view.View
import org.dokiteam.doki.list.ui.model.ListHeader

interface ListHeaderClickListener {

	fun onListHeaderClick(item: ListHeader, view: View)
}
