package org.dokiteam.doki.list.ui.adapter

import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegate
import org.dokiteam.doki.R
import org.dokiteam.doki.list.ui.model.ListModel
import org.dokiteam.doki.list.ui.model.LoadingFooter

fun loadingFooterAD() = adapterDelegate<LoadingFooter, ListModel>(R.layout.item_loading_footer) {
}