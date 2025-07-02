package org.dokiteam.doki.reader.ui

import org.dokiteam.doki.bookmarks.domain.Bookmark
import org.dokiteam.doki.parsers.model.MangaChapter
import org.dokiteam.doki.reader.ui.pager.ReaderPage

interface ReaderNavigationCallback {

	fun onPageSelected(page: ReaderPage): Boolean

	fun onChapterSelected(chapter: MangaChapter): Boolean

	fun onBookmarkSelected(bookmark: Bookmark): Boolean = onPageSelected(
		ReaderPage(bookmark.toMangaPage(), bookmark.page, bookmark.chapterId),
	)
}
