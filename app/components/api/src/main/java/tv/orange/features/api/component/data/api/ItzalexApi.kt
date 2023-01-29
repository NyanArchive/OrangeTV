package tv.orange.features.api.component.data.api

import io.reactivex.Single
import retrofit2.http.GET
import tv.orange.models.retrofit.itz.ItzBadgesResponseData
import tv.orange.models.retrofit.itz.ItzResponseData

interface ItzalexApi {
    @GET("/emotes")
    fun getEmotes(): Single<ItzResponseData>

    @GET("/badges")
    fun getBadges1(): Single<ItzBadgesResponseData>

    @GET("/badges2")
    fun getBadges2(): Single<ItzBadgesResponseData>
}