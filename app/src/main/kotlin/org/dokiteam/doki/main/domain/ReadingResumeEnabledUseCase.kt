package org.dokiteam.doki.main.domain

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import org.dokiteam.doki.core.model.isLocal
import org.dokiteam.doki.core.os.NetworkState
import org.dokiteam.doki.core.prefs.AppSettings
import org.dokiteam.doki.core.prefs.observeAsFlow
import org.dokiteam.doki.history.data.HistoryRepository
import javax.inject.Inject

class ReadingResumeEnabledUseCase @Inject constructor(
	private val networkState: NetworkState,
	private val historyRepository: HistoryRepository,
	private val settings: AppSettings,
) {

	operator fun invoke(): Flow<Boolean> = settings.observeAsFlow(AppSettings.KEY_INCOGNITO_MODE) {
		isIncognitoModeEnabled
	}.flatMapLatest { incognito ->
		if (incognito) {
			flowOf(false)
		} else {
			combine(networkState, historyRepository.observeLast()) { isOnline, last ->
				last != null && (isOnline || last.isLocal)
			}
		}
	}
}
