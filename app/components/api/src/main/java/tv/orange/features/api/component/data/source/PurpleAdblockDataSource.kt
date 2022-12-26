package tv.orange.features.api.component.data.source

import io.reactivex.Single
import retrofit2.Response
import tv.orange.features.api.component.data.api.PurpleAdblockApi
import javax.inject.Inject
import javax.inject.Named

class PurpleAdblockDataSource @Inject constructor(
    @Named("PAS1")
    val purpleS1AdblockApi: PurpleAdblockApi,
    @Named("PAS2")
    val purpleS2AdblockApi: PurpleAdblockApi
) {
    fun getS1Playlist(channelName: String): Single<Response<String>> {
        return purpleS1AdblockApi.getStreamPlaylist(channelName)
    }

    fun getS2Playlist(channelName: String): Single<Response<String>> {
        return purpleS2AdblockApi.getStreamPlaylist(channelName)
    }
}