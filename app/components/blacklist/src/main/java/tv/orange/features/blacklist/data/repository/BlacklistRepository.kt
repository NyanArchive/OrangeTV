package tv.orange.features.blacklist.data.repository

import io.reactivex.Completable
import io.reactivex.Observable
import tv.orange.features.blacklist.data.model.KeywordData
import tv.orange.features.blacklist.data.source.BlacklistSource
import javax.inject.Inject

class BlacklistRepository @Inject constructor(
    val source: BlacklistSource,
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

    fun addNewItems(rawText: String): Completable {
        return source.addRawText(rawText)
    }
}