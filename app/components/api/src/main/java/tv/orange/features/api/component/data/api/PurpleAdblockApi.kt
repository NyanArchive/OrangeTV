package tv.orange.features.api.component.data.api

import io.reactivex.Single
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface PurpleAdblockApi {
    @GET("/channel/{channelName}")
    fun getStreamPlaylist(
        @Path("channelName") channel: String,
    ): Single<Response<String>>
}