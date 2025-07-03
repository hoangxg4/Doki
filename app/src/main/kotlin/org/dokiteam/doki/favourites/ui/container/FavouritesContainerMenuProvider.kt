package org.dokiteam.doki.favourites.ui.container

import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.core.view.MenuProvider
import org.dokiteam.doki.R
import org.dokiteam.doki.core.nav.AppRouter

class FavouritesContainerMenuProvider(
	private val router: AppRouter,
) : MenuProvider {

	override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
		menuInflater.inflate(R.menu.opt_favourites_container, menu)
	}

	override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
		when (menuItem.itemId) {
			R.id.action_manage -> {
				router.openFavoriteCategories()
			}

			else -> return false
		}
		return true
	}
}
