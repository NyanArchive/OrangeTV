package tv.orange.features.api.component.data.api

import io.reactivex.Single
import retrofit2.http.GET
import tv.orange.models.retrofit.nop.Donations

interface NopApi {
    @GET("/orange/twitchmod/donations")
    fun globalBadges(): Single<Donations>

    @GET("/orange/homies")
    fun homiesBadges(): Single<HashMap<String, String>>
}