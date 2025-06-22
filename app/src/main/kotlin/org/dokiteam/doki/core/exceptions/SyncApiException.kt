package org.dokiteam.doki.core.exceptions

class SyncApiException(
	message: String,
	val code: Int,
) : RuntimeException(message)
