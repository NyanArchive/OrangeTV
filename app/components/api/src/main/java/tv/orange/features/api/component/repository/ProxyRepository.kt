package tv.orange.features.api.component.repository

import io.reactivex.Single
import retrofit2.Response
import tv.orange.core.util.RxUtil.nRetry
import tv.orange.features.api.component.data.source.LolDataSource
import tv.orange.features.api.component.data.source.PurpleAdblockDataSource
import tv.orange.features.api.component.data.source.TTSFTDataSource
import tv.orange.features.api.component.data.source.TwitchingDataSource
import tv.orange.features.api.di.scope.ApiScope
import javax.inject.Inject

@ApiScope
class ProxyRepository @Inject constructor(
    val ttsftDataSource: TTSFTDataSource,
    val twitchingDataSource: TwitchingDataSource,
    val lolDataSource: LolDataSource,
    val purpleAdblockDataSource: PurpleAdblockDataSource
) {
    fun getTwitchingPlaylist(
        channelName: String,
        sig: String,
        token: String
    ): Single<Response<String>> {
        return twitchingDataSource.getTwitchingPlaylist(channelName, sig, token).nRetry(3)
    }

    fun getTTSFTPlaylist(
        channelName: String,
        sig: String,
        token: String
    ): Single<Response<String>> {
        return ttsftDataSource.getTwitchingPlaylist(channelName, sig, token).nRetry(3)
    }

    fun getLolPlaylist(
        channelName: String
    ): Single<Response<String>> {
        return lolDataSource.getLolPlaylist(channelName).nRetry(3)
    }

    fun getPurplePlaylist(
        channelName: String
    ): Single<Response<String>> {
        return purpleAdblockDataSource.getS1Playlist(channelName).onErrorResumeNext(
            purpleAdblockDataSource.getS2Playlist(channelName)
        )
    }
}