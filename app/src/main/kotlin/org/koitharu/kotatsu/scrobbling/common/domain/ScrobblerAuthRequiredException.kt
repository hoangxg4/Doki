package org.dokiteam.doki.scrobbling.common.domain

import okio.IOException
import org.dokiteam.doki.scrobbling.common.domain.model.ScrobblerService

class ScrobblerAuthRequiredException(
	val scrobbler: ScrobblerService,
) : IOException()
