package tv.orange.features.api.component.data.api

import io.reactivex.Single
import retrofit2.http.GET
import tv.orange.models.retrofit.nop.DonationsData

interface NopApi {
    @GET("/orange/twitchmod/donations")
    fun globalBadges(): Single<DonationsData>

    @GET("/orange/homies")
    fun homiesBadges(): Single<HashMap<String, String>>
}