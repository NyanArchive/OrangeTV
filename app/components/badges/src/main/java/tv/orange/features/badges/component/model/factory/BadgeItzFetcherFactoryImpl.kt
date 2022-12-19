package tv.orange.features.badges.component.model.factory

import io.reactivex.Single
import tv.orange.models.data.SimpleFetcher
import tv.orange.models.data.badges.BadgeSet
import tv.orange.models.data.badges.impl.BadgeItzSet

data class BadgeItzFetcherFactoryImpl(
    private val provider: () -> Single<BadgeItzSet>
) : SimpleFetcher.SourceFactory<BadgeItzSet> {
    override fun create(): Single<BadgeItzSet> {
        return provider()
    }
}