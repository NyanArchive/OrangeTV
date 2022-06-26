package tv.orange.features.badges.component.data.sources

import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import tv.orange.features.badges.component.data.api.FfzApi
import tv.orange.features.badges.component.data.mapper.FfzBadgesApiMapper
import tv.orange.features.badges.component.data.models.BadgeSet
import javax.inject.Inject

class FfzRemoteDataSource @Inject constructor(
    val ffzApi: FfzApi,
    val ffzMapper: FfzBadgesApiMapper
) {
    fun getBadges(): Single<BadgeSet> {
        return ffzApi.globalBadges().subscribeOn(Schedulers.io()).map { badges ->
            ffzMapper.map(badges)
        }
    }
}