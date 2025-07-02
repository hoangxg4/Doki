package org.dokiteam.doki.favourites.domain

import dagger.Reusable
import kotlinx.coroutines.flow.Flow
import org.dokiteam.doki.core.db.MangaDatabase
import org.dokiteam.doki.core.db.entity.toManga
import org.dokiteam.doki.core.db.entity.toMangaTags
import org.dokiteam.doki.favourites.data.FavouriteManga
import org.dokiteam.doki.list.domain.ListFilterOption
import org.dokiteam.doki.list.domain.ListSortOrder
import org.dokiteam.doki.local.data.index.LocalMangaIndex
import org.dokiteam.doki.local.domain.LocalObserveMapper
import org.dokiteam.doki.parsers.model.Manga
import javax.inject.Inject

@Reusable
class LocalFavoritesObserver @Inject constructor(
	localMangaIndex: LocalMangaIndex,
	private val db: MangaDatabase,
) : LocalObserveMapper<FavouriteManga, Manga>(localMangaIndex) {

	fun observeAll(
		order: ListSortOrder,
		filterOptions: Set<ListFilterOption>,
		limit: Int
	): Flow<List<Manga>> = db.getFavouritesDao().observeAll(order, filterOptions, limit).mapToLocal()

	fun observeAll(
		categoryId: Long,
		order: ListSortOrder,
		filterOptions: Set<ListFilterOption>,
		limit: Int
	): Flow<List<Manga>> = db.getFavouritesDao().observeAll(categoryId, order, filterOptions, limit).mapToLocal()

	override fun toManga(e: FavouriteManga) = e.manga.toManga(e.tags.toMangaTags(), null)

	override fun toResult(e: FavouriteManga, manga: Manga) = manga
}
