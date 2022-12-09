package tv.orange.features.api.component.data.api

import io.reactivex.Maybe
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TwitchingApi {
    @GET("/{channelName}.m3u8")
    fun getStreamPlaylist(
        @Path("channelName") channel: String,
        @Query("sig") sig: String,
        @Query("token") token: String,
        @Query("allow_source") allowSource: Boolean = true,
        @Query("allow_audio_only") allowAudioOnly: Boolean = true,
        @Query("fast_bread") fastBread: Boolean = true,
        @Query("p") p: Int = 0,
        @Query("player_backend") player_backend: String = "site",
    ): Maybe<Response<String>>
}