package org.dokiteam.doki.settings.sources.catalog

import org.dokiteam.doki.parsers.model.ContentType

data class SourcesCatalogFilter(
	val types: Set<ContentType>,
	val locale: String?,
	val isNewOnly: Boolean,
)
