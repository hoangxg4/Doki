package org.dokiteam.doki.stats.ui

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.take
import org.dokiteam.doki.R
import org.dokiteam.doki.core.model.FavouriteCategory
import org.dokiteam.doki.core.ui.BaseViewModel
import org.dokiteam.doki.core.ui.util.ReversibleAction
import org.dokiteam.doki.core.util.ext.MutableEventFlow
import org.dokiteam.doki.core.util.ext.call
import org.dokiteam.doki.favourites.domain.FavouritesRepository
import org.dokiteam.doki.stats.data.StatsRepository
import org.dokiteam.doki.stats.domain.StatsPeriod
import org.dokiteam.doki.stats.domain.StatsRecord
import javax.inject.Inject

@HiltViewModel
class StatsViewModel @Inject constructor(
	private val repository: StatsRepository,
	favouritesRepository: FavouritesRepository,
) : BaseViewModel() {

	val period = MutableStateFlow(StatsPeriod.WEEK)
	val onActionDone = MutableEventFlow<ReversibleAction>()
	val selectedCategories = MutableStateFlow<Set<Long>>(emptySet())
	val favoriteCategories = favouritesRepository.observeCategories()
		.take(1)

	val readingStats = MutableStateFlow<List<StatsRecord>>(emptyList())

	init {
		launchJob(Dispatchers.Default) {
			combine<StatsPeriod, Set<Long>, Pair<StatsPeriod, Set<Long>>>(
				period,
				selectedCategories,
				::Pair,
			).collectLatest { p ->
				readingStats.value = withLoading {
					repository.getReadingStats(p.first, p.second)
				}
			}
		}
	}

	fun setCategoryChecked(category: FavouriteCategory, checked: Boolean) {
		val snapshot = selectedCategories.value.toMutableSet()
		if (checked) {
			snapshot.add(category.id)
		} else {
			snapshot.remove(category.id)
		}
		selectedCategories.value = snapshot
	}

	fun clearStats() {
		launchLoadingJob(Dispatchers.Default) {
			repository.clearStats()
			readingStats.value = emptyList()
			onActionDone.call(ReversibleAction(R.string.stats_cleared, null))
		}
	}
}
