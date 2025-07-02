package org.dokiteam.doki.settings

import android.content.Context
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.core.view.MenuProvider
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView
import leakcanary.LeakCanary
import org.dokiteam.doki.DokiApp
import org.dokiteam.doki.R
import org.koitharu.workinspector.WorkInspector

class SettingsMenuProvider(
	private val context: Context,
) : MenuProvider {

	private val application: DokiApp
		get() = context.applicationContext as DokiApp

	override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
		menuInflater.inflate(R.menu.opt_settings, menu)
	}

	override fun onPrepareMenu(menu: Menu) {
		super.onPrepareMenu(menu)
		menu.findItem(R.id.action_leakcanary).isChecked = application.isLeakCanaryEnabled
		menu.findItem(R.id.action_ssiv_debug).isChecked = SubsamplingScaleImageView.isDebug
	}

	override fun onMenuItemSelected(menuItem: MenuItem): Boolean = when (menuItem.itemId) {
		R.id.action_leaks -> {
			context.startActivity(LeakCanary.newLeakDisplayActivityIntent())
			true
		}

		R.id.action_works -> {
			context.startActivity(WorkInspector.getIntent(context))
			true
		}

		R.id.action_leakcanary -> {
			val checked = !menuItem.isChecked
			menuItem.isChecked = checked
			application.isLeakCanaryEnabled = checked
			true
		}

		R.id.action_ssiv_debug -> {
			val checked = !menuItem.isChecked
			menuItem.isChecked = checked
			SubsamplingScaleImageView.isDebug = checked
			true
		}

		else -> false
	}
}
