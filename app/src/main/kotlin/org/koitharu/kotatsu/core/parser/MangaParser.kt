package org.dokiteam.doki.core.parser

import org.dokiteam.doki.parsers.MangaLoaderContext
import org.dokiteam.doki.parsers.MangaParser
import org.dokiteam.doki.parsers.model.MangaParserSource

fun MangaParser(source: MangaParserSource, loaderContext: MangaLoaderContext): MangaParser {
	return when (source) {
		MangaParserSource.DUMMY -> DummyParser(loaderContext)
		else -> loaderContext.newParserInstance(source)
	}
}
