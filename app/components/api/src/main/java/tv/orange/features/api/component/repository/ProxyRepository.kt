package tv.orange.features.api.component.repository

import io.reactivex.Maybe
import retrofit2.Response
import tv.orange.features.api.component.data.source.TTSFTDataSource
import tv.orange.features.api.component.data.source.TwitchingDataSource
import tv.orange.features.api.di.scope.ApiScope
import javax.inject.Inject

@ApiScope
class ProxyRepository @Inject constructor(
    val ttsftDataSource: TTSFTDataSource,
    val twitchingDataSource: TwitchingDataSource
) {
    fun getTwitchingPlaylist(
        channelName: String,
        sig: String,
        token: String
    ): Maybe<Response<String>> {
        return twitchingDataSource.getTwitchingPlaylist(channelName, sig, token)
    }

    fun getTTSFTPlaylist(
        channelName: String,
        sig: String,
        token: String
    ): Maybe<Response<String>> {
        return ttsftDataSource.getTwitchingPlaylist(channelName, sig, token)
    }
}