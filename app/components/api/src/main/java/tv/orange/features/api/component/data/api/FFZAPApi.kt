package tv.orange.features.api.component.data.api

import io.reactivex.Single
import retrofit2.http.GET
import tv.orange.models.retrofit.ffzap.FfzAPBadge

interface FFZAPApi {
    @GET("/v1/supporters")
    fun supporters(): Single<List<FfzAPBadge>>
}