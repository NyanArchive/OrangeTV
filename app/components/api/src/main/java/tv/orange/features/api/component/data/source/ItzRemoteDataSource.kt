package tv.orange.features.api.component.data.source

import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import tv.orange.features.api.component.data.api.ItzalexApi
import tv.orange.features.api.component.data.mapper.ItzApiMapper
import tv.orange.models.data.emotes.Emote
import javax.inject.Inject

class ItzRemoteDataSource @Inject constructor(
    val itzalexApi: ItzalexApi,
    val itzApiMapper: ItzApiMapper
) {
    fun getGlobalEmotes(): Single<List<Emote>> {
        return itzalexApi.getEmotes().subscribeOn(Schedulers.io()).map { data ->
            itzApiMapper.mapEmotes(data).first
        }
    }

    fun getChannelEmotes(channelId: Int): Single<List<Emote>> {
        return itzalexApi.getEmotes().subscribeOn(Schedulers.io()).map { data ->
            itzApiMapper.mapEmotes(data).second[channelId] ?: emptyList()
        }
    }
}