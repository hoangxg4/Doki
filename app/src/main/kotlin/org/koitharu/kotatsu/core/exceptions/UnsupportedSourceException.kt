package org.dokiteam.doki.core.exceptions

import org.dokiteam.doki.parsers.model.Manga

class UnsupportedSourceException(
	message: String?,
	val manga: Manga?,
) : IllegalArgumentException(message)
