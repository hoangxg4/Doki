package org.dokiteam.doki.details.domain

import org.dokiteam.doki.core.parser.MangaRepository
import org.dokiteam.doki.core.util.ext.printStackTraceDebug
import org.dokiteam.doki.parsers.model.Manga
import org.dokiteam.doki.parsers.util.runCatchingCancellable
import javax.inject.Inject

class RelatedMangaUseCase @Inject constructor(
	private val mangaRepositoryFactory: MangaRepository.Factory,
) {

	suspend operator fun invoke(seed: Manga) = runCatchingCancellable {
		mangaRepositoryFactory.create(seed.source).getRelated(seed)
	}.onFailure {
		it.printStackTraceDebug()
	}.getOrNull()
}
