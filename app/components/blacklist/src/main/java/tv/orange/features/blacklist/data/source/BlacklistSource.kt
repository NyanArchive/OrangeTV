package tv.orange.features.blacklist.data.source

import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import tv.orange.features.blacklist.data.mapper.BlacklistMapper
import tv.orange.features.blacklist.data.model.KeywordData
import tv.orange.features.blacklist.db.BlacklistDatabase
import javax.inject.Inject

class BlacklistSource @Inject constructor(
    val mapper: BlacklistMapper
) {
    private val db = BlacklistDatabase.INSTANCE

    fun getFlow(): Observable<List<KeywordData>> {
        return db.highlighterDAO().getFlow().subscribeOn(Schedulers.io()).map(mapper::map)
    }

    fun delete(keyword: KeywordData): Completable {
        return db.highlighterDAO().delete(
            word = keyword.word,
            type = keyword.type.name
        ).subscribeOn(Schedulers.io())
    }

    fun add(keyword: KeywordData): Completable {
        return db.highlighterDAO().insert(
            keywords = listOf(
                mapper.map(
                    keyword = keyword
                )
            )
        ).subscribeOn(Schedulers.io())
    }

    fun changeType(item: KeywordData, newType: KeywordData.Type): Completable {
        return db.highlighterDAO().get(
            word = item.word,
            type = item.type.name
        ).subscribeOn(Schedulers.io()).flatMapCompletable { entity ->
            db.highlighterDAO().update(entity.apply {
                this.type = newType.name
            })
        }
    }

    fun get(keyword: KeywordData): Maybe<KeywordData> {
        return get(word = keyword.word, type = keyword.type.name)
    }

    fun get(word: String, type: String): Maybe<KeywordData> {
        return db.highlighterDAO().get(
            word = word,
            type = type
        ).map(mapper::map).subscribeOn(Schedulers.io())
    }

    fun addRawText(rawText: String): Completable {
        return Completable.concat(mapper.map(
            rawText = rawText
        ).map { keyword ->
            get(
                word = keyword.word,
                type = keyword.type.name
            ).subscribeOn(Schedulers.io())
                .switchIfEmpty(add(keyword).toMaybe())
                .flatMapCompletable {
                    Completable.complete()
                }
        }).subscribeOn(Schedulers.io())
    }
}