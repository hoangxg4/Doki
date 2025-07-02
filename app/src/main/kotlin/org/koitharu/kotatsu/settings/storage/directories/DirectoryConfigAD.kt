package org.dokiteam.doki.settings.storage.directories

import androidx.appcompat.widget.TooltipCompat
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegateViewBinding
import org.dokiteam.doki.R
import org.dokiteam.doki.core.ui.list.OnListItemClickListener
import org.dokiteam.doki.core.util.ext.drawableStart
import org.dokiteam.doki.core.util.ext.textAndVisible
import org.dokiteam.doki.databinding.ItemStorageConfigBinding
import org.dokiteam.doki.settings.storage.DirectoryModel

fun directoryConfigAD(
	clickListener: OnListItemClickListener<DirectoryModel>,
) = adapterDelegateViewBinding<DirectoryModel, DirectoryModel, ItemStorageConfigBinding>(
	{ layoutInflater, parent -> ItemStorageConfigBinding.inflate(layoutInflater, parent, false) },
) {

	binding.buttonRemove.setOnClickListener { v -> clickListener.onItemClick(item, v) }
	TooltipCompat.setTooltipText(binding.buttonRemove, binding.buttonRemove.contentDescription)

	bind {
		binding.textViewTitle.text = item.title ?: getString(item.titleRes)
		binding.textViewSubtitle.textAndVisible = item.file?.absolutePath
		binding.buttonRemove.isVisible = item.isRemovable
		binding.buttonRemove.isEnabled = !item.isChecked
		binding.textViewTitle.drawableStart = if (!item.isAvailable) {
			ContextCompat.getDrawable(context, R.drawable.ic_alert_outline)?.apply {
				setTint(ContextCompat.getColor(context, R.color.warning))
			}
		} else if (item.isChecked) {
			ContextCompat.getDrawable(context, R.drawable.ic_download)
		} else {
			null
		}
	}
}
