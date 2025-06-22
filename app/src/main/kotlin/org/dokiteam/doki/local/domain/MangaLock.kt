package org.dokiteam.doki.local.domain

import org.koitharu.kotatsu.core.util.MultiMutex
import org.koitharu.kotatsu.parsers.model.Manga
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MangaLock @Inject constructor() : MultiMutex<Manga>()
