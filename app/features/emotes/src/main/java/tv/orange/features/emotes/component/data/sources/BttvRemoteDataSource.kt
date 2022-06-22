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
}