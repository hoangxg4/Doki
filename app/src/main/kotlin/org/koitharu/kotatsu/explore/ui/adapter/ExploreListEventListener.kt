package org.dokiteam.doki.explore.ui.adapter

import android.view.View
import org.dokiteam.doki.list.ui.adapter.ListHeaderClickListener
import org.dokiteam.doki.list.ui.adapter.ListStateHolderListener

interface ExploreListEventListener : ListStateHolderListener, View.OnClickListener, ListHeaderClickListener
