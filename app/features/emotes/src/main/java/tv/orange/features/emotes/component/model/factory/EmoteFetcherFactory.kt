package tv.orange.features.emotes.component.model.factory

import io.reactivex.Single
import tv.orange.models.data.emotes.EmoteSet

interface EmoteFetcherFactory {
    fun create(): Single<EmoteSet>
}