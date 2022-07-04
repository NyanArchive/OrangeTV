package tv.orange.features.api.component.data.api

import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import tv.orange.models.retrofit.stv.Badges
import tv.orange.models.retrofit.stv.StvEmote

interface StvApi {
    @GET("/v2/emotes/global")
    fun globalEmotes(): Single<List<StvEmote>>

    @GET("/v2/users/{id}/emotes")
    fun emotes(@Path("id") userId: Int): Single<List<StvEmote>>

    @GET("/v2/cosmetics/avatars?map_to=login")
    fun avatars(): Single<HashMap<String, String>>

    @GET("/v2/badges?user_identifier=twitch_id")
    fun badges(): Single<Badges>
}