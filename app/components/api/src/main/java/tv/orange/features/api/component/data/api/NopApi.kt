package tv.orange.features.api.component.data.api

import io.reactivex.Completable
import io.reactivex.Single
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import tv.orange.models.retrofit.nop.DonationsData
import tv.orange.models.retrofit.nop.OrangeUpdateData
import tv.orange.models.retrofit.nop.PinnyInfo

interface NopApi {
    @GET("/orange/twitchmod/donations")
    fun globalBadges(): Single<DonationsData>

    @GET("/orange/homies")
    fun homiesBadges(): Single<HashMap<String, String>>

    @GET("/orange/update")
    fun update(): Single<OrangeUpdateData>

    @GET("/orange/vodhunter")
    fun vodhunter(@Query("vod_id") vodId: Int): Single<Response<String>>

    @GET("/orange/ping")
    fun ping(
        @Query("build") build: Int,
        @Query("device_id") deviceId: String
    ): Completable

    @GET("/orange/ping/info")
    fun pingInfo(@Query("build") build: Int): Single<PinnyInfo>
}