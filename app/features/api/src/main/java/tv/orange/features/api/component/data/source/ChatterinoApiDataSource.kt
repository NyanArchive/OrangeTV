package tv.orange.features.api.component.data.source

import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import tv.orange.features.api.component.data.api.ChatterinoApi
import tv.orange.features.api.component.data.mapper.ChatterinoApiMapper
import tv.orange.models.data.badges.BadgeSet
import javax.inject.Inject

class ChatterinoApiDataSource @Inject constructor(
    val chatterinoApi: ChatterinoApi,
    val chatterinoApiMapper: ChatterinoApiMapper
) {
    fun getBadges(): Single<BadgeSet> {
        return chatterinoApi.getBadges().subscribeOn(Schedulers.io()).map { badges ->
            chatterinoApiMapper.mapBadges(badges = badges)
        }
    }
}