package tv.orange.features.badges.component.repository

import io.reactivex.Single
import tv.orange.features.badges.component.data.models.BadgeSet
import tv.orange.features.badges.component.data.sources.FfzRemoteDataSource
import javax.inject.Inject

class BadgeRepository @Inject constructor(
    val ffzDataSource: FfzRemoteDataSource
) {
    fun getFfzBadges(): Single<BadgeSet> {
        return ffzDataSource.getBadges()
    }
}