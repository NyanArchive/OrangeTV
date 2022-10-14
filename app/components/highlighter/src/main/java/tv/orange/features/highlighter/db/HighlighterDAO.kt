package tv.orange.features.highlighter.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Observable
import tv.orange.features.highlighter.db.entities.KeywordEntity

@Dao
interface HighlighterDAO {
    @Query("SELECT * FROM keywords")
    fun getFlow(): Observable<List<KeywordEntity>>

    @Query("SELECT * FROM keywords WHERE word = :word AND type = :type")
    fun get(word: String, type: String): Maybe<KeywordEntity>

    @Insert
    fun insert(keywords: List<KeywordEntity>): Completable

    @Query("DELETE FROM keywords WHERE word = :word AND type = :type")
    fun delete(word: String, type: String): Completable

    @Update
    fun update(keyword: KeywordEntity): Completable
}