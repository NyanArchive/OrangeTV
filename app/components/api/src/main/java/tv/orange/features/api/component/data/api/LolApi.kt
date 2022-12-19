package tv.orange.features.api.component.data.api

import io.reactivex.Single
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path

interface LolApi {
    @GET("/playlist/{channelName}.m3u8%3Fallow_source%3Dtrue%26fast_bread%3Dtrue%26allow_audio_only%3Dtrue")
    @Headers(
        "accept: application/x-mpegURL, application/vnd.apple.mpegurl, application/json, text/plain",
        "x-donate-to: https://ttv.lol/donate",
    )
    fun getPlaylist(
        @Path("channelName") channel: String
    ): Single<Response<String>>
}