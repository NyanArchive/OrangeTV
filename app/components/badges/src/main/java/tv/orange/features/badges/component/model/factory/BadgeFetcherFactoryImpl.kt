package tv.orange.features.badges.component.model.factory

import io.reactivex.Single
import tv.orange.models.data.SimpleFetcher
import tv.orange.models.data.badges.BadgeSet

data class BadgeFetcherFactoryImpl(
    private val provider: () -> Single<BadgeSet>
) : SimpleFetcher.SourceFactory<BadgeSet> {
    override fun create(): Single<BadgeSet> {
        return provider()
    }
}