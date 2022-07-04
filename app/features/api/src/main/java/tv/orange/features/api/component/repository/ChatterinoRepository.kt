package tv.orange.features.api.component.repository

import io.reactivex.Single
import tv.orange.features.api.component.data.source.ChatterinoApiDataSource
import tv.orange.features.api.di.scope.ApiScope
import tv.orange.models.data.badges.BadgeSet
import javax.inject.Inject

@ApiScope
class ChatterinoRepository @Inject constructor(
    val chatterinoApiDataSource: ChatterinoApiDataSource
) {
    fun getChatterinoBadges(): Single<BadgeSet> {
        return chatterinoApiDataSource.getBadges()
    }
}