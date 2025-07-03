package org.dokiteam.doki.local.ui

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.BackoffPolicy
import androidx.work.CoroutineWorker
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.OutOfQuotaPolicy
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import androidx.work.await
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import org.dokiteam.doki.core.parser.MangaDataRepository
import org.dokiteam.doki.core.prefs.AppSettings
import org.dokiteam.doki.local.data.LocalMangaRepository
import org.dokiteam.doki.local.domain.DeleteReadChaptersUseCase
import java.util.concurrent.TimeUnit

@HiltWorker
class LocalStorageCleanupWorker @AssistedInject constructor(
	@Assisted appContext: Context,
	@Assisted params: WorkerParameters,
	private val settings: AppSettings,
	private val localMangaRepository: LocalMangaRepository,
	private val dataRepository: MangaDataRepository,
	private val deleteReadChaptersUseCase: DeleteReadChaptersUseCase,
) : CoroutineWorker(appContext, params) {

	override suspend fun doWork(): Result {
		if (settings.isAutoLocalChaptersCleanupEnabled) {
			deleteReadChaptersUseCase.invoke()
		}
		return if (localMangaRepository.cleanup()) {
			dataRepository.cleanupLocalManga()
			Result.success()
		} else {
			Result.retry()
		}
	}

	companion object {

		private const val TAG = "cleanup"

		suspend fun enqueue(context: Context) {
			val request = OneTimeWorkRequestBuilder<LocalStorageCleanupWorker>()
				.addTag(TAG)
				.setBackoffCriteria(BackoffPolicy.LINEAR, 10, TimeUnit.MINUTES)
				.setExpedited(OutOfQuotaPolicy.RUN_AS_NON_EXPEDITED_WORK_REQUEST)
				.build()
			WorkManager.getInstance(context).enqueueUniqueWork(TAG, ExistingWorkPolicy.KEEP, request).await()
		}
	}
}
