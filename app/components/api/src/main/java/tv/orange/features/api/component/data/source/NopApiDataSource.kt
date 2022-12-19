package tv.orange.features.api.component.data.source

import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import retrofit2.Response
import tv.orange.features.api.component.data.api.NopApi
import tv.orange.features.api.component.data.mapper.NopApiMapper
import tv.orange.models.data.badges.BadgeSet
import tv.orange.models.data.badges.impl.BadgeItzSet
import tv.orange.models.retrofit.nop.OrangeUpdateData
import tv.orange.models.retrofit.nop.PinnyInfo
import javax.inject.Inject

class NopApiDataSource @Inject constructor(
    val nopApi: NopApi,
    val nopApiMapper: NopApiMapper
) {
    fun getBadges(): Single<BadgeSet> {
        return nopApi.globalBadges().subscribeOn(Schedulers.io()).map(nopApiMapper::map)
    }

    fun getHomiesBadges(): Single<BadgeItzSet> {
        return nopApi.homiesBadges().subscribeOn(Schedulers.io()).map(nopApiMapper::map)
    }

    fun ping(build: Int, deviceId: String): Completable {
        return nopApi.ping(build, deviceId)
    }

    fun pingInfo(build: Int): Single<PinnyInfo> {
        return nopApi.pingInfo(build)
    }

    fun getOrangeUpdate(): Single<OrangeUpdateData> {
        return nopApi.update()
    }

    fun getVodHunterPlaylist(vodId: Int): Single<Response<String>> {
        return nopApi.vodhunter(vodId = vodId)
    }
}