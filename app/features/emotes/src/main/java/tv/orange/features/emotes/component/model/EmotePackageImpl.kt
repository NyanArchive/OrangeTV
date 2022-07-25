package tv.orange.features.emotes.component.model

import tv.orange.core.LoggerWithTag
import tv.orange.models.SimpleFetcher
import tv.orange.models.abs.EmotePackageSet
import tv.orange.models.data.emotes.Emote
import tv.orange.models.data.emotes.EmoteSet

class EmotePackageImpl(
    source: SourceFactory<EmoteSet>,
    private val token: EmotePackageSet
) : SimpleFetcher<EmoteSet>(
    dataSourceFactory = source,
    logger = LoggerWithTag(token.toString())
), EmotePackage {
    override fun getEmote(code: String): Emote? {
        return getData()?.getEmote(code)
    }

    override fun getEmotes(): List<Emote> {
        return getData()?.getEmotes() ?: emptyList()
    }

    override fun isEmpty(): Boolean {
        return getData()?.isEmpty() ?: true
    }

    override fun getToken(): EmotePackageSet {
        return token
    }
}