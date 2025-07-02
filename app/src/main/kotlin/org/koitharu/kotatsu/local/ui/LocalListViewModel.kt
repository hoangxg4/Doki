package org.dokiteam.doki.local.ui

import android.content.SharedPreferences
import androidx.lifecycle.SavedStateHandle
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharedFlow
import org.dokiteam.doki.R
import org.dokiteam.doki.core.parser.MangaDataRepository
import org.dokiteam.doki.core.parser.MangaRepository
import org.dokiteam.doki.core.prefs.AppSettings
import org.dokiteam.doki.core.prefs.ListMode
import org.dokiteam.doki.core.util.ext.MutableEventFlow
import org.dokiteam.doki.core.util.ext.call
import org.dokiteam.doki.core.util.ext.toFileOrNull
import org.dokiteam.doki.core.util.ext.toUriOrNull
import org.dokiteam.doki.explore.data.MangaSourcesRepository
import org.dokiteam.doki.explore.domain.ExploreRepository
import org.dokiteam.doki.filter.ui.FilterCoordinator
import org.dokiteam.doki.list.domain.MangaListMapper
import org.dokiteam.doki.list.ui.model.EmptyState
import org.dokiteam.doki.list.ui.model.ListModel
import org.dokiteam.doki.list.ui.model.MangaListModel
import org.dokiteam.doki.list.ui.model.TipModel
import org.dokiteam.doki.local.data.LocalStorageChanges
import org.dokiteam.doki.local.data.LocalStorageManager
import org.dokiteam.doki.local.domain.DeleteLocalMangaUseCase
import org.dokiteam.doki.local.domain.model.LocalManga
import org.dokiteam.doki.parsers.model.Manga
import org.dokiteam.doki.remotelist.ui.RemoteListViewModel
import javax.inject.Inject

@HiltViewModel
class LocalListViewModel @Inject constructor(
	savedStateHandle: SavedStateHandle,
	mangaRepositoryFactory: MangaRepository.Factory,
	filterCoordinator: FilterCoordinator,
	private val settings: AppSettings,
	mangaListMapper: MangaListMapper,
	private val deleteLocalMangaUseCase: DeleteLocalMangaUseCase,
	exploreRepository: ExploreRepository,
	@LocalStorageChanges private val localStorageChanges: SharedFlow<LocalManga?>,
	private val localStorageManager: LocalStorageManager,
	sourcesRepository: MangaSourcesRepository,
	mangaDataRepository: MangaDataRepository,
) : RemoteListViewModel(
	savedStateHandle = savedStateHandle,
	mangaRepositoryFactory = mangaRepositoryFactory,
	filterCoordinator = filterCoordinator,
	settings = settings,
	mangaListMapper = mangaListMapper,
	exploreRepository = exploreRepository,
	sourcesRepository = sourcesRepository,
	mangaDataRepository = mangaDataRepository,
), SharedPreferences.OnSharedPreferenceChangeListener {

	val onMangaRemoved = MutableEventFlow<Unit>()

	init {
		launchJob(Dispatchers.Default) {
			localStorageChanges
				.collect {
					loadList(filterCoordinator.snapshot(), append = false).join()
				}
		}
		settings.subscribe(this)
	}

	override suspend fun onBuildList(list: MutableList<ListModel>) {
		super.onBuildList(list)
		if (localStorageManager.hasExternalStoragePermission(isReadOnly = true)) {
			return
		}
		for (item in list) {
			if (item !is MangaListModel) {
				continue
			}
			val file = item.manga.url.toUriOrNull()?.toFileOrNull() ?: continue
			if (localStorageManager.isOnExternalStorage(file)) {
				val tip = TipModel(
					key = "permission",
					title = R.string.external_storage,
					text = R.string.missing_storage_permission,
					icon = R.drawable.ic_storage,
					primaryButtonText = R.string.fix,
					secondaryButtonText = R.string.settings,
				)
				list.add(0, tip)
				return
			}
		}
	}

	override fun onCleared() {
		settings.unsubscribe(this)
		super.onCleared()
	}

	override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
		if (key == AppSettings.KEY_LOCAL_MANGA_DIRS) {
			onRefresh()
		}
	}

	fun delete(ids: Set<Long>) {
		launchLoadingJob(Dispatchers.Default) {
			deleteLocalMangaUseCase(ids)
			onMangaRemoved.call(Unit)
		}
	}

	override suspend fun mapMangaList(
		destination: MutableCollection<in ListModel>,
		manga: Collection<Manga>,
		mode: ListMode
	) = mangaListMapper.toListModelList(destination, manga, mode, MangaListMapper.NO_SAVED)

	override fun createEmptyState(canResetFilter: Boolean): EmptyState = if (canResetFilter) {
		super.createEmptyState(true)
	} else {
		EmptyState(
			icon = R.drawable.ic_empty_local,
			textPrimary = R.string.text_local_holder_primary,
			textSecondary = R.string.text_local_holder_secondary,
			actionStringRes = R.string._import,
		)
	}
}
