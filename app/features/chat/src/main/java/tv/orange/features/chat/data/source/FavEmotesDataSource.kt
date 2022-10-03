package tv.orange.features.chat.data.source

import io.reactivex.Completable
import io.reactivex.Single
import tv.orange.features.chat.db.FavEmotesDatabase
import tv.orange.features.chat.db.entities.FavEmoteEntity
import tv.orange.models.abc.EmotePackageSet
import tv.orange.models.abc.OrangeEmoteType
import javax.inject.Inject

class FavEmotesDataSource @Inject constructor(
    val db: FavEmotesDatabase
) {
    fun addEmote(
        channelId: String,
        emoteCode: String,
        emoteId: String?,
        packageSet: EmotePackageSet
    ): Completable {
        return db.favEmotesDAO().insert(
            listOf(
                FavEmoteEntity(
                    isTwitchEmote = packageSet.type == OrangeEmoteType.TWITCH,
                    emoteId = emoteId,
                    emoteCode = emoteCode,
                    channelId = if (packageSet.isGlobal) {
                        "-1"
                    } else {
                        channelId
                    },
                    emoteType = packageSet.type.name
                )
            )
        )
    }

    fun delete(uid: Int): Completable {
        return db.favEmotesDAO().deleteByUid(uid = uid)
    }

    fun getChannelEmotes(channelId: Int): Single<List<FavEmoteEntity>> {
        return db.favEmotesDAO().getForChannel(channelId = channelId.toString())
    }
}