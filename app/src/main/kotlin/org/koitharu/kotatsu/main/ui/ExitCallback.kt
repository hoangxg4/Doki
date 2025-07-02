package org.dokiteam.doki.main.ui

import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.lifecycleScope
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.dokiteam.doki.R
import org.dokiteam.doki.core.prefs.AppSettings
import org.dokiteam.doki.core.prefs.observeAsFlow
import org.dokiteam.doki.main.ui.owners.BottomNavOwner

class ExitCallback(
	private val activity: MainActivity,
	private val snackbarHost: View,
) : OnBackPressedCallback(false) {

	private var job: Job? = null

	init {
		observeSettings()
	}

	override fun handleOnBackPressed() {
		job?.cancel()
		job = activity.lifecycleScope.launch {
			resetExitConfirmation()
		}
	}

	private suspend fun resetExitConfirmation() {
		isEnabled = false
		val snackbar = Snackbar.make(snackbarHost, R.string.confirm_exit, Snackbar.LENGTH_INDEFINITE)
		snackbar.anchorView = (activity as? BottomNavOwner)?.bottomNav
		snackbar.show()
		delay(2000)
		snackbar.dismiss()
		isEnabled = true
	}

	private fun observeSettings() {
		activity.settings
			.observeAsFlow(AppSettings.KEY_EXIT_CONFIRM) { isExitConfirmationEnabled }
			.flowOn(Dispatchers.Default)
			.onEach { isEnabled = it }
			.launchIn(activity.lifecycleScope)
	}
}
