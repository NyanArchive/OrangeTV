package tv.orange.features.emotes.component.repository

import io.reactivex.Single
import tv.orange.features.emotes.component.data.sources.BttvRemoteDataSource
import tv.orange.features.emotes.models.Emote
import javax.inject.Inject

class EmoteRepository @Inject constructor(val bttvDataSource: BttvRemoteDataSource) {
    fun getBttvGlobalEmotes(): Single<List<Emote>> {
        return bttvDataSource.getGlobalEmotes()
    }

    fun getFfzGlobalEmotes(): Single<List<Emote>> {
        return bttvDataSource.getGlobalFfzEmotes()
    }

    fun getBttvChannelEmotes(channelId: Int): Single<List<Emote>> {
        return bttvDataSource.getChannelBttvEmotes(channelId)
    }

    fun getFfzChannelEmotes(channelId: Int): Single<List<Emote>> {
        return bttvDataSource.getChannelFfzEmotes(channelId)
    }
}