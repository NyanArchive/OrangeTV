package tv.orange.features.api.component.data.source

import io.reactivex.Maybe
import io.reactivex.Single
import retrofit2.Response
import tv.orange.features.api.component.data.api.TwitchingApi
import javax.inject.Inject

class TwitchingDataSource @Inject constructor(
    val twitchingApi: TwitchingApi
) {
    fun getTwitchingPlaylist(
        channelName: String, sig: String, token: String
    ): Single<Response<String>> {
        return twitchingApi.getStreamPlaylist(channelName, sig, token)
    }
}