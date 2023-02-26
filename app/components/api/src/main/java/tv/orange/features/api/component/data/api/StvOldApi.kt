package tv.orange.features.api.component.data.api

import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import tv.orange.models.retrofit.stv.BadgesData
import tv.orange.models.retrofit.stv.StvEmote
import tv.orange.models.retrofit.stv.v3.EmoteSet
import tv.orange.models.retrofit.stv.v3.User

interface StvOldApi {
    @GET("/v2/badges?user_identifier=twitch_id")
    fun badges(): Single<BadgesData>
}