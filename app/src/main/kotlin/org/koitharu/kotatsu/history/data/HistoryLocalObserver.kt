package org.dokiteam.doki.history.data

import dagger.Reusable
import org.dokiteam.doki.core.db.MangaDatabase
import org.dokiteam.doki.core.db.entity.toManga
import org.dokiteam.doki.core.db.entity.toMangaTags
import org.dokiteam.doki.history.domain.model.MangaWithHistory
import org.dokiteam.doki.list.domain.ListFilterOption
import org.dokiteam.doki.list.domain.ListSortOrder
import org.dokiteam.doki.local.data.index.LocalMangaIndex
import org.dokiteam.doki.local.domain.LocalObserveMapper
import org.dokiteam.doki.parsers.model.Manga
import javax.inject.Inject

@Reusable
class HistoryLocalObserver @Inject constructor(
	localMangaIndex: LocalMangaIndex,
	private val db: MangaDatabase,
) : LocalObserveMapper<HistoryWithManga, MangaWithHistory>(localMangaIndex) {

	fun observeAll(
		order: ListSortOrder,
		filterOptions: Set<ListFilterOption>,
		limit: Int
	) = db.getHistoryDao().observeAll(order, filterOptions, limit).mapToLocal()

	override fun toManga(e: HistoryWithManga) = e.manga.toManga(e.tags.toMangaTags(), null)

	override fun toResult(e: HistoryWithManga, manga: Manga) = MangaWithHistory(
		manga = manga,
		history = e.history.toMangaHistory(),
	)
}
