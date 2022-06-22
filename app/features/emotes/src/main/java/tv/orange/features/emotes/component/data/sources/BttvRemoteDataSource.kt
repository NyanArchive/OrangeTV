package tv.orange.features.emotes.component.data.sources

import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import tv.orange.features.emotes.component.data.api.BttvApi
import tv.orange.features.emotes.component.data.mapper.BttvEmotesApiMapper
import tv.orange.features.emotes.models.Emote
import javax.inject.Inject
import javax.inject.Named

class BttvRemoteDataSource @Inject constructor(
    val bttvApi: BttvApi,
    val bttvMapper: BttvEmotesApiMapper
) {
    fun getGlobalEmotes(): Single<List<Emote>> {
        return bttvApi.globalBttvEmotes().subscribeOn(Schedulers.io()).map { emotes ->
            bttvMapper.map(emotes)
        }
    }

    fun getGlobalFfzEmotes(): Single<List<Emote>> {
        return bttvApi.globalFfzEmotes().subscribeOn(Schedulers.io()).map { emotes ->
            bttvMapper.mapFfz(emotes)
        }
    }

    fun getChannelBttvEmotes(channelId: Int): Single<List<Emote>> {
        return bttvApi.bttvEmotes(channelId).subscribeOn(Schedulers.io()).map { emotes ->
            bttvMapper.mapChannel(emotes)
        }
    }

    fun getChannelFfzEmotes(channelId: Int): Single<List<Emote>> {
        return bttvApi.ffzEmotes(channelId).subscribeOn(Schedulers.io()).map { emotes ->
            bttvMapper.mapFfz(emotes)
        }
    }
}