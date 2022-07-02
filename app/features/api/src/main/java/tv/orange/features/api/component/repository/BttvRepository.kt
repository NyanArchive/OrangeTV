package tv.orange.features.api.component.repository

import io.reactivex.Single
import tv.orange.features.api.component.data.source.BttvRemoteDataSource
import tv.orange.features.api.di.scope.ApiScope
import tv.orange.models.data.emotes.Emote
import javax.inject.Inject

@ApiScope
class BttvRepository @Inject constructor(
    val bttvDataSource: BttvRemoteDataSource
) {
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