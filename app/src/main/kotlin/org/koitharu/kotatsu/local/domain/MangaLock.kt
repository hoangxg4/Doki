package org.dokiteam.doki.local.domain

import org.dokiteam.doki.core.util.MultiMutex
import org.dokiteam.doki.parsers.model.Manga
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MangaLock @Inject constructor() : MultiMutex<Manga>()
