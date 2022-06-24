package tv.orange.features.emotes.component.data.sources

import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import tv.orange.features.emotes.component.data.api.StvApi
import tv.orange.features.emotes.component.data.mapper.StvEmotesApiMapper
import tv.orange.features.emotes.models.Emote
import javax.inject.Inject

class StvRemoteDataSource @Inject constructor(
    val stvApi: StvApi,
    val stvMapper: StvEmotesApiMapper
) {
    fun getGlobalEmotes(): Single<List<Emote>> {
        return stvApi.globalEmotes().subscribeOn(Schedulers.io()).map { emotes ->
            stvMapper.map(emotes)
        }
    }

    fun getChannelEmotes(channelId: Int): Single<List<Emote>> {
        return stvApi.channelEmotes(channelId).subscribeOn(Schedulers.io()).map { emotes ->
            stvMapper.map(emotes)
        }
    }
}