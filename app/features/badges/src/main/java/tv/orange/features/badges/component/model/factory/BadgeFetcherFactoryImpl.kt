package tv.orange.features.badges.component.model.factory

import io.reactivex.Single
import tv.orange.models.data.badges.BadgeSet

data class BadgeFetcherFactoryImpl(
    private val provider: () -> Single<BadgeSet>,
    val desc: String
) :
    BadgeFetcherFactory {
    override fun create(): Single<BadgeSet> {
        return provider()
    }
}