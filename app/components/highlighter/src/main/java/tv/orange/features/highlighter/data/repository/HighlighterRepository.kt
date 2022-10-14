package tv.orange.features.highlighter.data.repository

import io.reactivex.Completable
import io.reactivex.Observable
import tv.orange.features.highlighter.data.model.KeywordData
import tv.orange.features.highlighter.data.source.HighlighterSource
import javax.inject.Inject

class HighlighterRepository @Inject constructor(
    val source: HighlighterSource,
) {
    fun getFlow(): Observable<List<KeywordData>> {
        return source.getFlow()
    }

    fun delete(keyword: KeywordData): Completable {
        return source.delete(keyword)
    }

    fun add(keyword: KeywordData): Completable {
        return source.add(keyword)
    }

    fun changeType(item: KeywordData, newType: KeywordData.Type): Completable {
        return source.changeType(item, newType)
    }

    fun changeColor(item: KeywordData, newColor: KeywordData.Color): Completable {
        return source.changeColor(item, newColor)
    }

    fun addNewItems(rawText: String): Completable {
        return source.addRawText(rawText)
    }
}