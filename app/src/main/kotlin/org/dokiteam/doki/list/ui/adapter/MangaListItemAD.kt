package org.dokiteam.doki.list.ui.adapter

import androidx.appcompat.widget.TooltipCompat
import androidx.core.view.isVisible
import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegateViewBinding
import org.dokiteam.doki.core.ui.list.AdapterDelegateClickListenerAdapter
import org.dokiteam.doki.core.ui.list.OnListItemClickListener
import org.dokiteam.doki.core.util.ext.textAndVisible
import org.dokiteam.doki.databinding.ItemMangaListBinding
import org.dokiteam.doki.list.ui.model.ListModel
import org.dokiteam.doki.list.ui.model.MangaCompactListModel
import org.dokiteam.doki.parsers.model.Manga

fun mangaListItemAD(
	clickListener: OnListItemClickListener<Manga>,
) = adapterDelegateViewBinding<MangaCompactListModel, ListModel, ItemMangaListBinding>(
	{ inflater, parent -> ItemMangaListBinding.inflate(inflater, parent, false) },
) {

	AdapterDelegateClickListenerAdapter(this, clickListener, MangaCompactListModel::manga).attach(itemView)

	bind {
		TooltipCompat.setTooltipText(itemView, item.getSummary(context))
		binding.textViewTitle.text = item.title
		binding.textViewSubtitle.textAndVisible = item.subtitle
		binding.imageViewCover.setImageAsync(item.coverUrl, item.manga)
		binding.badge.number = item.counter
		binding.badge.isVisible = item.counter > 0
	}
}
