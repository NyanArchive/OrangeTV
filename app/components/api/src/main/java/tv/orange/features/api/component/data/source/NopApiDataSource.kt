package tv.orange.features.api.component.data.source

import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import tv.orange.features.api.component.data.api.NopApi
import tv.orange.features.api.component.data.mapper.NopApiMapper
import tv.orange.models.data.badges.BadgeSet
import javax.inject.Inject

class NopApiDataSource @Inject constructor(
    val nopApi: NopApi,
    val nopApiMapper: NopApiMapper
) {
    fun getBadges(): Single<BadgeSet> {
        return nopApi.globalBadges().subscribeOn(Schedulers.io()).map(nopApiMapper::map)
    }

    fun getHomiesBadges(): Single<BadgeSet> {
        return nopApi.homiesBadges().subscribeOn(Schedulers.io()).map(nopApiMapper::map)
    }

    fun ping(build: Int, deviceId: String): Completable {
        return nopApi.ping(build, deviceId)
    }
}