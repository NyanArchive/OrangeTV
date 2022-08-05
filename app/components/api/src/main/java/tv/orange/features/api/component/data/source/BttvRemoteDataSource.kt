package tv.orange.features.api.component.data.source

import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import tv.orange.core.Logger
import tv.orange.features.api.component.data.api.BttvApi
import tv.orange.features.api.component.data.mapper.BttvApiMapper
import tv.orange.models.data.emotes.Emote
import javax.inject.Inject

class BttvRemoteDataSource @Inject constructor(
    val bttvApi: BttvApi,
    val bttvMapper: BttvApiMapper
) {
    fun getGlobalEmotes(): Single<List<Emote>> {
        return bttvApi.globalBttvEmotes().subscribeOn(Schedulers.io()).map { emotes ->
            bttvMapper.mapBttvEmotes(emotes, false)
        }
    }

    fun getGlobalFfzEmotes(): Single<List<Emote>> {
        return bttvApi.globalFfzEmotes().subscribeOn(Schedulers.io()).map { emotes ->
            bttvMapper.mapFfzEmotes(emotes, false)
        }
    }

    fun getChannelBttvEmotes(channelId: Int): Single<List<Emote>> {
        return bttvApi.bttvEmotes(channelId).subscribeOn(Schedulers.io()).map { emotes ->
            bttvMapper.mapChannelEmotes(emotes)
        }.onErrorResumeNext {
            Logger.warning("Cannot fetch emotes for channel: $channelId")
            Single.just(emptyList())
        }
    }

    fun getChannelFfzEmotes(channelId: Int): Single<List<Emote>> {
        return bttvApi.ffzEmotes(channelId).subscribeOn(Schedulers.io()).map { emotes ->
            bttvMapper.mapFfzEmotes(emotes, true)
        }.onErrorResumeNext {
            Logger.warning("Cannot fetch emotes for channel: $channelId")
            Single.just(emptyList())
        }
    }
}