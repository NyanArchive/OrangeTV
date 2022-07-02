package tv.orange.features.emotes.component.model.factory

import io.reactivex.Single
import tv.orange.models.data.emotes.EmoteSet

class EmoteFetcherFactoryImpl(
    private val provider: () -> Single<EmoteSet>,
    val sourceName: String,
    val channelId: Int
) :
    EmoteFetcherFactory {
    override fun create(): Single<EmoteSet> {
        return provider()
    }

    override fun name(): String {
        return sourceName
    }

    override fun channelId(): Int {
        return channelId
    }
}