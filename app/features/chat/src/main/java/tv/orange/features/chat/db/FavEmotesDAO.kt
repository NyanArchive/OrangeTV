package tv.orange.features.chat.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import tv.orange.features.chat.db.entities.FavEmoteEntity

@Dao
interface FavEmotesDAO {
    @Query("SELECT * FROM emotes")
    fun getAll(): Single<List<FavEmoteEntity>>

    @Query("SELECT * FROM emotes WHERE (channelId = :channelId AND channelId != 1) OR isGlobalEmote = 1 OR (isTwitchEmote = 1 AND channelId = -1)")
    fun getForChannel(channelId: String): Single<List<FavEmoteEntity>>

    @Insert
    fun insert(favEmoteEntity: List<FavEmoteEntity>): Completable

    @Query("DELETE FROM emotes WHERE emoteType = :type AND channelId = :channelId AND emoteCode = :code")
    fun delete(type: String, channelId: String, code: String): Completable
}