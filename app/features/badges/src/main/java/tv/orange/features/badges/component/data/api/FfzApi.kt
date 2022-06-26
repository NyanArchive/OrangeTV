package tv.orange.features.badges.component.data.api

import io.reactivex.Single
import retrofit2.http.GET
import tv.orange.models.retrofit.ffz.FfzBadges

interface FfzApi {
    @GET("/v1/badges/ids")
    fun globalBadges(): Single<FfzBadges>
}