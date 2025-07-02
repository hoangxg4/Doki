package org.dokiteam.doki.tracker.domain

import org.dokiteam.doki.core.prefs.AppSettings
import org.dokiteam.doki.favourites.domain.FavouritesRepository
import org.dokiteam.doki.list.domain.ListFilterOption
import org.dokiteam.doki.list.domain.MangaListQuickFilter
import javax.inject.Inject

class UpdatesListQuickFilter @Inject constructor(
	private val favouritesRepository: FavouritesRepository,
	settings: AppSettings,
) : MangaListQuickFilter(settings) {

	override suspend fun getAvailableFilterOptions(): List<ListFilterOption> =
		favouritesRepository.getMostUpdatedCategories(
			limit = 4,
		).map {
			ListFilterOption.Favorite(it)
		}
}
