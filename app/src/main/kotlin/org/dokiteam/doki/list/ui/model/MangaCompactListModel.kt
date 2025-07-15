package org.dokiteam.doki.list.ui.model

import org.dokiteam.doki.core.ui.model.MangaOverride
import org.dokiteam.doki.parsers.model.Manga

data class MangaCompactListModel(
	override val manga: Manga,
	override val override: MangaOverride?,
	val subtitle: String,
	override val counter: Int,
) : MangaListModel()
