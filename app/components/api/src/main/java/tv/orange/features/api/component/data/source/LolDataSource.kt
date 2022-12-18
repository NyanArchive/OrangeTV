package tv.orange.features.api.component.data.source

import io.reactivex.Single
import retrofit2.Response
import tv.orange.features.api.component.data.api.LolApi
import javax.inject.Inject

class LolDataSource @Inject constructor(
    val lolApi: LolApi
) {
    fun getLolPlaylist(channelName: String): Single<Response<String>> {
        return lolApi.getPlaylist(channelName)
    }
}