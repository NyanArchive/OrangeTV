package tv.orange.features.api.component.data.api

import io.reactivex.Single
import retrofit2.http.GET
import tv.orange.models.retrofit.chatterino.Badges

interface ChatterinoApi {
    @GET("/badges")
    fun getBadges(): Single<Badges>
}