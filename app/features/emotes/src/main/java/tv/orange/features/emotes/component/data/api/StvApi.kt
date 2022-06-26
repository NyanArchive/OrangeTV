package tv.orange.features.emotes.component.data.api

import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import tv.orange.models.retrofit.stv.StvEmote

interface StvApi {
    @GET("/v2/emotes/global")
    fun globalEmotes(): Single<List<StvEmote>>

    @GET("/v2/users/{id}/emotes")
    fun emotes(@Path("id") userId: Int): Single<List<StvEmote>>
}