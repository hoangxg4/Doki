package org.dokiteam.doki.tracker.ui.feed

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.plus
import org.dokiteam.doki.R
import org.dokiteam.doki.core.prefs.AppSettings
import org.dokiteam.doki.core.prefs.ListMode
import org.dokiteam.doki.core.prefs.observeAsFlow
import org.dokiteam.doki.core.prefs.observeAsStateFlow
import org.dokiteam.doki.core.ui.BaseViewModel
import org.dokiteam.doki.core.ui.model.DateTimeAgo
import org.dokiteam.doki.core.ui.util.ReversibleAction
import org.dokiteam.doki.core.util.ext.MutableEventFlow
import org.dokiteam.doki.core.util.ext.calculateTimeAgo
import org.dokiteam.doki.core.util.ext.call
import org.dokiteam.doki.list.domain.ListFilterOption
import org.dokiteam.doki.list.domain.MangaListMapper
import org.dokiteam.doki.list.domain.QuickFilterListener
import org.dokiteam.doki.list.ui.model.EmptyState
import org.dokiteam.doki.list.ui.model.ListHeader
import org.dokiteam.doki.list.ui.model.ListModel
import org.dokiteam.doki.list.ui.model.LoadingState
import org.dokiteam.doki.list.ui.model.toErrorState
import org.dokiteam.doki.tracker.domain.TrackingRepository
import org.dokiteam.doki.tracker.domain.UpdatesListQuickFilter
import org.dokiteam.doki.tracker.domain.model.TrackingLogItem
import org.dokiteam.doki.tracker.ui.feed.model.FeedItem
import org.dokiteam.doki.tracker.ui.feed.model.UpdatedMangaHeader
import org.dokiteam.doki.tracker.work.TrackWorker
import java.util.concurrent.atomic.AtomicBoolean
import javax.inject.Inject

private const val PAGE_SIZE = 20

@HiltViewModel
class FeedViewModel @Inject constructor(
	private val settings: AppSettings,
	private val repository: TrackingRepository,
	private val scheduler: TrackWorker.Scheduler,
	private val mangaListMapper: MangaListMapper,
	private val quickFilter: UpdatesListQuickFilter,
) : BaseViewModel(), QuickFilterListener by quickFilter {

	private val limit = MutableStateFlow(PAGE_SIZE)
	private val isReady = AtomicBoolean(false)

	val isRunning = scheduler.observeIsRunning()
		.stateIn(viewModelScope + Dispatchers.Default, SharingStarted.Lazily, false)

	val isHeaderEnabled = settings.observeAsStateFlow(
		scope = viewModelScope + Dispatchers.Default,
		key = AppSettings.KEY_FEED_HEADER,
		valueProducer = { isFeedHeaderVisible },
	)

	val onActionDone = MutableEventFlow<ReversibleAction>()

	@Suppress("USELESS_CAST")
	val content = combine(
		observeHeader(),
		quickFilter.appliedOptions,
		combine(limit, quickFilter.appliedOptions.combineWithSettings(), ::Pair)
			.flatMapLatest { repository.observeTrackingLog(it.first, it.second) },
	) { header, filters, list ->
		val result = ArrayList<ListModel>((list.size * 1.4).toInt().coerceAtLeast(3))
		quickFilter.filterItem(filters)?.let(result::add)
		if (header != null) {
			result += header
		}
		if (list.isEmpty()) {
			result += EmptyState(
				icon = R.drawable.ic_empty_feed,
				textPrimary = R.string.text_empty_holder_primary,
				textSecondary = R.string.text_feed_holder,
				actionStringRes = 0,
			)
		} else {
			isReady.set(true)
			list.mapListTo(result)
		}
		result as List<ListModel>
	}.catch { e ->
		emit(listOf(e.toErrorState(canRetry = false)))
	}.stateIn(viewModelScope + Dispatchers.Default, SharingStarted.Eagerly, listOf(LoadingState))

	init {
		launchJob(Dispatchers.Default) {
			repository.gc()
		}
	}

	fun clearFeed(clearCounters: Boolean) {
		launchLoadingJob(Dispatchers.Default) {
			repository.clearLogs()
			if (clearCounters) {
				repository.clearCounters()
			}
			onActionDone.call(ReversibleAction(R.string.updates_feed_cleared, null))
		}
	}

	fun requestMoreItems() {
		if (isReady.compareAndSet(true, false)) {
			limit.value += PAGE_SIZE
		}
	}

	fun update() {
		scheduler.startNow()
	}

	fun setHeaderEnabled(value: Boolean) {
		settings.isFeedHeaderVisible = value
	}

	fun onItemClick(item: FeedItem) {
		launchJob(Dispatchers.Default, CoroutineStart.ATOMIC) {
			repository.markAsRead(item.id)
		}
	}

	private suspend fun List<TrackingLogItem>.mapListTo(destination: MutableList<ListModel>) {
		var prevDate: DateTimeAgo? = null
		for (item in this) {
			val date = calculateTimeAgo(item.createdAt)
			if (prevDate != date) {
				destination += if (date != null) {
					ListHeader(date)
				} else {
					ListHeader(R.string.unknown)
				}
			}
			prevDate = date
			destination += mangaListMapper.toFeedItem(item)
		}
	}

	private fun observeHeader() = isHeaderEnabled.flatMapLatest { hasHeader ->
		if (hasHeader) {
			quickFilter.appliedOptions.combineWithSettings().flatMapLatest {
				repository.observeUpdatedManga(10, it)
			}.map { mangaList ->
				if (mangaList.isEmpty()) {
					null
				} else {
					UpdatedMangaHeader(
						mangaList.map { mangaListMapper.toListModel(it.manga, ListMode.GRID) },
					)
				}
			}
		} else {
			flowOf(null)
		}
	}

	private fun Flow<Set<ListFilterOption>>.combineWithSettings(): Flow<Set<ListFilterOption>> = combine(
		settings.observeAsFlow(AppSettings.KEY_DISABLE_NSFW) { isNsfwContentDisabled },
	) { filters, skipNsfw ->
		if (skipNsfw) {
			filters + ListFilterOption.SFW
		} else {
			filters
		}
	}
}
