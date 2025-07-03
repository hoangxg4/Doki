package org.dokiteam.doki.core.parser.favicon

import android.net.Uri
import org.dokiteam.doki.parsers.model.MangaSource

const val URI_SCHEME_FAVICON = "favicon"

fun MangaSource.faviconUri(): Uri = Uri.fromParts(URI_SCHEME_FAVICON, name, null)