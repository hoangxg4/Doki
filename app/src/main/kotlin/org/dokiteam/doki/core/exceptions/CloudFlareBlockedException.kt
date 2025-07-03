package org.dokiteam.doki.core.exceptions

import org.dokiteam.doki.core.model.UnknownMangaSource
import org.dokiteam.doki.parsers.model.MangaSource
import org.dokiteam.doki.parsers.network.CloudFlareHelper

class CloudFlareBlockedException(
	override val url: String,
	source: MangaSource?,
) : CloudFlareException("Blocked by CloudFlare", CloudFlareHelper.PROTECTION_BLOCKED) {

	override val source: MangaSource = source ?: UnknownMangaSource
}
