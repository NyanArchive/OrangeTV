package tv.orange.features.api.component.data.source

import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import tv.orange.features.api.component.data.api.FfzApi
import tv.orange.features.api.component.data.mapper.FfzApiMapper
import tv.orange.models.data.badges.BadgeSet
import javax.inject.Inject

class FfzRemoteDataSource @Inject constructor(
    val ffzApi: FfzApi,
    val ffzMapper: FfzApiMapper
) {
    fun getBadges(): Single<BadgeSet> {
        return ffzApi.globalBadges().subscribeOn(Schedulers.io()).map(ffzMapper::mapBadges)
    }
}