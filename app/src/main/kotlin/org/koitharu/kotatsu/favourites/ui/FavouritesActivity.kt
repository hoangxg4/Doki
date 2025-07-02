package org.dokiteam.doki.favourites.ui

import android.os.Bundle
import org.dokiteam.doki.core.nav.AppRouter
import org.dokiteam.doki.core.ui.FragmentContainerActivity
import org.dokiteam.doki.favourites.ui.list.FavouritesListFragment

class FavouritesActivity : FragmentContainerActivity(FavouritesListFragment::class.java) {

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		val categoryTitle = intent.getStringExtra(AppRouter.KEY_TITLE)
		if (categoryTitle != null) {
			title = categoryTitle
		}
	}
}
