package org.dokiteam.doki.scrobbling.common.domain

import org.dokiteam.doki.scrobbling.anilist.data.AniListRepository
import org.dokiteam.doki.scrobbling.common.data.ScrobblerRepository
import org.dokiteam.doki.scrobbling.common.domain.model.ScrobblerService
import org.dokiteam.doki.scrobbling.kitsu.data.KitsuRepository
import org.dokiteam.doki.scrobbling.mal.data.MALRepository
import org.dokiteam.doki.scrobbling.shikimori.data.ShikimoriRepository
import javax.inject.Inject
import javax.inject.Provider

class ScrobblerRepositoryMap @Inject constructor(
	private val shikimoriRepository: Provider<ShikimoriRepository>,
	private val aniListRepository: Provider<AniListRepository>,
	private val malRepository: Provider<MALRepository>,
	private val kitsuRepository: Provider<KitsuRepository>,
) {

	operator fun get(scrobblerService: ScrobblerService): ScrobblerRepository = when (scrobblerService) {
		ScrobblerService.SHIKIMORI -> shikimoriRepository
		ScrobblerService.ANILIST -> aniListRepository
		ScrobblerService.MAL -> malRepository
		ScrobblerService.KITSU -> kitsuRepository
	}.get()
}
