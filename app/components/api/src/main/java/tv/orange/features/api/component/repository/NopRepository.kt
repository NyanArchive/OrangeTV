package tv.orange.features.api.component.repository

import io.reactivex.Completable
import io.reactivex.Single
import tv.orange.features.api.component.data.source.NopApiDataSource
import tv.orange.features.api.di.scope.ApiScope
import tv.orange.models.data.badges.BadgeSet
import javax.inject.Inject

@ApiScope
class NopRepository @Inject constructor(
    val nopApiDataSource: NopApiDataSource
) {
    fun getTwitchModBadges(): Single<BadgeSet> {
        return nopApiDataSource.getBadges()
    }

    fun getHomiesBadges(): Single<BadgeSet> {
        return nopApiDataSource.getHomiesBadges()
    }

    fun ping(build: Int, deviceId: String): Completable {
        return nopApiDataSource.ping(build = build, deviceId = deviceId)
    }
}