package org.dokiteam.doki.reader.ui

import org.dokiteam.doki.reader.ui.pager.ReaderPage

data class ReaderContent(
	val pages: List<ReaderPage>,
	val state: ReaderState?
)