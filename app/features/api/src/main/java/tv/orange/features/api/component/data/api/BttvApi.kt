package tv.orange.features.api.component.data.api

import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import tv.orange.models.retrofit.bttv.BttvChannel
import tv.orange.models.retrofit.bttv.BttvEmote
import tv.orange.models.retrofit.bttv.FfzEmote

interface BttvApi {
    @GET("/3/cached/emotes/global")
    fun globalBttvEmotes(): Single<List<BttvEmote>>

    @GET("/3/cached/frankerfacez/emotes/global")
    fun globalFfzEmotes(): Single<List<FfzEmote>>

    @GET("/3/cached/users/twitch/{id}")
    fun bttvEmotes(@Path("id") channelId: Int): Single<BttvChannel>

    @GET("/3/cached/frankerfacez/users/twitch/{id}")
    fun ffzEmotes(@Path("id") channelId: Int): Single<List<FfzEmote>>
}