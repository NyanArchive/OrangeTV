package tv.orange.features.api.component.data.source

import io.reactivex.Single
import retrofit2.Response
import tv.orange.features.api.component.data.api.TTSFTApi
import javax.inject.Inject

class TTSFTDataSource @Inject constructor(
    val ttsftApi: TTSFTApi
) {
    fun getTwitchingPlaylist(
        channelName: String, sig: String, token: String
    ): Single<Response<String>> {
        return ttsftApi.getStreamPlaylist(channelName, sig, token)
    }
}