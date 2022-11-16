package tv.orange.features.api.component.data.api

import io.reactivex.Single
import retrofit2.http.GET
import tv.orange.models.retrofit.itz.ItzResponseData

interface ItzalexApi {
    @GET("/emotes")
    fun getEmotes(): Single<ItzResponseData>
}