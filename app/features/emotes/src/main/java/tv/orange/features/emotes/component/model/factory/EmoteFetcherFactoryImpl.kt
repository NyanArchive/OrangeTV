package tv.orange.features.emotes.component.model.factory

import io.reactivex.Single
import tv.orange.models.data.emotes.EmoteSet

class EmoteFetcherFactoryImpl(private val provider: () -> Single<EmoteSet>) : EmoteFetcherFactory {
    override fun create(): Single<EmoteSet> {
        return provider()
    }
}