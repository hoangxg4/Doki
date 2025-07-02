package org.dokiteam.doki.list.ui.adapter

import org.dokiteam.doki.list.domain.ListFilterOption

interface QuickFilterClickListener {

	fun onFilterOptionClick(option: ListFilterOption)
}
