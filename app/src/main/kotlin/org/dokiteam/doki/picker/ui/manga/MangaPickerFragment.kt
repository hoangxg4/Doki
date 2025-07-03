package org.dokiteam.doki.picker.ui.manga

import android.view.View
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import org.dokiteam.doki.R
import org.dokiteam.doki.list.ui.MangaListFragment
import org.dokiteam.doki.parsers.model.Manga
import org.dokiteam.doki.picker.ui.PageImagePickActivity

@AndroidEntryPoint
class MangaPickerFragment : MangaListFragment() {

	override val isSwipeRefreshEnabled = false

	override val viewModel by viewModels<MangaPickerViewModel>()

	override fun onScrolledToEnd() = Unit

	override fun onItemClick(item: Manga, view: View) {
		(activity as PageImagePickActivity).onMangaPicked(item)
	}

	override fun onResume() {
		super.onResume()
		activity?.setTitle(R.string.pick_manga_page)
	}

	override fun onItemLongClick(item: Manga, view: View): Boolean = false

	override fun onItemContextClick(item: Manga, view: View): Boolean = false
}
