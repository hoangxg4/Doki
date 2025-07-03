package org.dokiteam.doki.details.domain

import org.dokiteam.doki.core.util.LocaleStringComparator
import org.dokiteam.doki.details.ui.model.MangaBranch

class BranchComparator : Comparator<MangaBranch> {

	private val delegate = LocaleStringComparator()

	override fun compare(o1: MangaBranch, o2: MangaBranch): Int = delegate.compare(o1.name, o2.name)
}
