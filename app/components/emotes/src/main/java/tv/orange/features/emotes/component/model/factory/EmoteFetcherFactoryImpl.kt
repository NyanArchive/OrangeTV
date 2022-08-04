package tv.orange.features.emotes.component.model.factory

import io.reactivex.Single
import tv.orange.models.SimpleFetcher
import tv.orange.models.data.emotes.EmoteSet

data class EmoteFetcherFactoryImpl(
    private val provider: () -> Single<EmoteSet>
) : SimpleFetcher.SourceFactory<EmoteSet> {
    override fun create(): Single<EmoteSet> {
        return provider()
    }
}