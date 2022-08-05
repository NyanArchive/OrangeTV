package tv.orange.features.api.component.data.api

import io.reactivex.Single
import retrofit2.http.GET
import tv.orange.models.retrofit.ffz.FfzBadgesData

interface FfzApi {
    @GET("/v1/badges/ids")
    fun globalBadges(): Single<FfzBadgesData>
}