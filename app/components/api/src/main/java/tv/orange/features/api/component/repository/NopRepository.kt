package tv.orange.features.api.component.repository

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
}