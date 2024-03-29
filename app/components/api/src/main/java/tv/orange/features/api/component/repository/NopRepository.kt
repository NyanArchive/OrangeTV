package tv.orange.features.api.component.repository

import io.reactivex.Completable
import io.reactivex.Single
import retrofit2.Response
import tv.orange.features.api.component.data.source.NopApiDataSource
import tv.orange.features.api.di.scope.ApiScope
import tv.orange.models.data.badges.BadgeSet
import tv.orange.models.data.badges.impl.BadgeItzSet
import tv.orange.models.retrofit.nop.OrangeUpdateData
import tv.orange.models.retrofit.nop.PinnyInfo
import javax.inject.Inject

@ApiScope
class NopRepository @Inject constructor(
    val nopApiDataSource: NopApiDataSource
) {
    fun getTwitchModBadges(): Single<BadgeSet> {
        return nopApiDataSource.getBadges()
    }

    fun getUpdateData(): Single<OrangeUpdateData> {
        return nopApiDataSource.getOrangeUpdate()
    }

    fun getHomiesBadges(): Single<BadgeItzSet> {
        return nopApiDataSource.getHomiesBadges()
    }

    fun ping(buildNumber: Int, deviceId: String): Completable {
        return nopApiDataSource.ping(build = buildNumber, deviceId = deviceId)
    }

    fun pingInfo(buildNumber: Int): Single<PinnyInfo> {
        return nopApiDataSource.pingInfo(build = buildNumber)
    }

    fun getVodhunterPlaylist(vodId: Int): Single<Response<String>> {
        return nopApiDataSource.getVodHunterPlaylist(vodId = vodId)
    }
}