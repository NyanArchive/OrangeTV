package tv.orange.features.api.component.data.api

import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import tv.orange.models.retrofit.stv.BadgesData
import tv.orange.models.retrofit.stv.StvEmote
import tv.orange.models.retrofit.stv.v3.EmoteSet
import tv.orange.models.retrofit.stv.v3.User

interface StvApi {
    @GET("/v3/emote-sets/global")
    fun globalEmotes(): Single<EmoteSet>

    @GET("/v3/users/twitch/{id}")
    fun emotes(@Path("id") userId: Int): Single<User>

    @GET("/v2/cosmetics/avatars?map_to=login")
    fun avatars(): Single<HashMap<String, String>>
}