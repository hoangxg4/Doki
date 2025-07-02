package org.dokiteam.doki.core.exceptions

import okhttp3.Headers
import org.dokiteam.doki.core.model.UnknownMangaSource
import org.dokiteam.doki.parsers.model.MangaSource
import org.dokiteam.doki.parsers.network.CloudFlareHelper

class CloudFlareProtectedException(
	override val url: String,
	source: MangaSource?,
	@Transient val headers: Headers,
) : CloudFlareException("Protected by CloudFlare", CloudFlareHelper.PROTECTION_CAPTCHA) {

	override val source: MangaSource = source ?: UnknownMangaSource
}
