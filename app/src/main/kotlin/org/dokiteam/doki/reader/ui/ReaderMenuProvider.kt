package org.dokiteam.doki.reader.ui

import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.core.view.MenuProvider
import org.dokiteam.doki.R

class ReaderMenuProvider(
	private val viewModel: ReaderViewModel,
	private val onOptionsClick: () -> Unit
) : MenuProvider {

	override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
		menuInflater.inflate(R.menu.opt_reader, menu)
	}

	override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
		return when (menuItem.itemId) {
			R.id.action_info -> {
				// TODO
				true
			}
			R.id.action_options -> {
				onOptionsClick()
				true
			}
			else -> false
		}
	}
}
