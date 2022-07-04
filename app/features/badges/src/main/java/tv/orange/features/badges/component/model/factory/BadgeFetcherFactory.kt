package tv.orange.features.badges.component.model.factory

import io.reactivex.Single
import tv.orange.models.data.badges.BadgeSet

interface BadgeFetcherFactory {
    fun create(): Single<BadgeSet>
}