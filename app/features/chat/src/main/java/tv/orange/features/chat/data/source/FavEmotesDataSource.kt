package tv.orange.features.chat.data.source

import io.reactivex.Completable
import io.reactivex.Single
import tv.orange.features.chat.data.mapper.FavEmotesMapper
import tv.orange.features.chat.data.model.FavEmote
import tv.orange.features.chat.db.FavEmotesDatabase
import javax.inject.Inject

class FavEmotesDataSource @Inject constructor(
    val mapper: FavEmotesMapper,
    val db: FavEmotesDatabase
) {
    fun addEmote(favEmote: FavEmote): Completable {
        return db.favEmotesDAO().insert(listOf(mapper.mapEmote(favEmote)))
    }

    fun delete(type: String, channelId: String, code: String): Completable {
        return db.favEmotesDAO().delete(type, channelId, code)
    }

    fun getChannelEmotes(channelId: Int): Single<List<FavEmote>> {
        return db.favEmotesDAO().getForChannel(channelId = channelId.toString())
            .map { mapper.mapEmotes(it) }
    }
}