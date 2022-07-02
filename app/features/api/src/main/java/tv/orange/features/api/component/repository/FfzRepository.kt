package tv.orange.features.api.component.repository

import io.reactivex.Single
import tv.orange.features.api.component.data.source.FfzRemoteDataSource
import tv.orange.features.api.di.scope.ApiScope
import tv.orange.models.data.badges.BadgeSet
import javax.inject.Inject

@ApiScope
class FfzRepository @Inject constructor(
    val ffzDataSource: FfzRemoteDataSource
) {
    fun getFfzBadges(): Single<BadgeSet> {
        return ffzDataSource.getBadges()
    }
}