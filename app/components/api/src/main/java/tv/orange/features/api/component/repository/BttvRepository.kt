package tv.orange.features.api.component.repository

import io.reactivex.Single
import tv.orange.features.api.component.data.source.BttvRemoteDataSource
import tv.orange.features.api.di.scope.ApiScope
import tv.orange.models.data.emotes.EmoteSet
import javax.inject.Inject

@ApiScope
class BttvRepository @Inject constructor(
    val bttvDataSource: BttvRemoteDataSource
) {
    fun getBttvGlobalEmotes(): Single<EmoteSet> {
        return bttvDataSource.getGlobalEmotes().map { EmoteSet(it) }
    }

    fun getFfzGlobalEmotes(): Single<EmoteSet> {
        return bttvDataSource.getGlobalFfzEmotes().map { EmoteSet(it) }
    }

    fun getBttvChannelEmotes(channelId: Int): Single<EmoteSet> {
        return bttvDataSource.getChannelBttvEmotes(channelId).map { EmoteSet(it) }
    }

    fun getFfzChannelEmotes(channelId: Int): Single<EmoteSet> {
        return bttvDataSource.getChannelFfzEmotes(channelId).map { EmoteSet(it) }
    }
}