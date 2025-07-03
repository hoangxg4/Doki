package org.dokiteam.doki.settings.sources.adapter

import org.dokiteam.doki.core.ui.ReorderableListAdapter
import org.dokiteam.doki.settings.sources.model.SourceConfigItem

class SourceConfigAdapter(
	listener: SourceConfigListener,
) : ReorderableListAdapter<SourceConfigItem>() {

	init {
		with(delegatesManager) {
			addDelegate(sourceConfigItemDelegate2(listener))
			addDelegate(sourceConfigEmptySearchDelegate())
			addDelegate(sourceConfigTipDelegate(listener))
		}
	}
}
