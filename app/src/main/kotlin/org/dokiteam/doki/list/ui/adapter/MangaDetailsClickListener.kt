package org.dokiteam.doki.list.ui.adapter

import android.view.View
import org.dokiteam.doki.core.ui.list.OnListItemClickListener
import org.dokiteam.doki.list.ui.model.MangaListModel
import org.dokiteam.doki.parsers.model.Manga
import org.dokiteam.doki.parsers.model.MangaTag

interface MangaDetailsClickListener : OnListItemClickListener<MangaListModel> {

	fun onReadClick(manga: Manga, view: View)

	fun onTagClick(manga: Manga, tag: MangaTag, view: View)
}
