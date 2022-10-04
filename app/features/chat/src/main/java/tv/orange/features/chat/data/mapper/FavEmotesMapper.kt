package tv.orange.features.chat.data.mapper

import tv.orange.features.chat.data.model.FavEmote
import tv.orange.features.chat.data.model.OrangeFavEmote
import tv.orange.features.chat.data.model.TwitchFavEmote
import tv.orange.features.chat.db.entities.FavEmoteEntity
import tv.orange.models.abc.EmotePackageSet
import javax.inject.Inject

class FavEmotesMapper @Inject constructor() {
    fun mapEmote(it: FavEmote): FavEmoteEntity {
        return when (it) {
            is OrangeFavEmote -> {
                FavEmoteEntity(
                    isTwitchEmote = false,
                    emoteId = null,
                    emoteCode = it.getCode(),
                    channelId = it.getChannelId().toString(),
                    emoteType = it.getPackageSet().name,
                    isAnimated = it.isAnimated(),
                    isGlobalEmote = it.getPackageSet().isGlobal
                )
            }
            is TwitchFavEmote -> {
                FavEmoteEntity(
                    isTwitchEmote = true,
                    emoteId = it.emoteId,
                    emoteCode = it.getCode(),
                    channelId = it.getChannelId().toString(),
                    emoteType = it.getPackageSet().name,
                    isAnimated = it.isAnimated(),
                    isGlobalEmote = it.getPackageSet().isGlobal
                )
            }
        }
    }

    fun mapEmotes(it: List<FavEmoteEntity>): List<FavEmote> {
        return it.mapNotNull { entity ->
            if (entity.isTwitchEmote) {
                entity.emoteId ?: return@mapNotNull null
                return@mapNotNull TwitchFavEmote(
                    code = entity.emoteCode,
                    isAnimated = entity.isAnimated,
                    channelId = entity.channelId.toIntOrNull() ?: -1,
                    packageSet = EmotePackageSet.from(entity.emoteType),
                    emoteId = entity.emoteId
                )
            } else {
                return@mapNotNull OrangeFavEmote(
                    code = entity.emoteCode,
                    isAnimated = entity.isAnimated,
                    channelId = entity.channelId.toIntOrNull() ?: -1,
                    packageSet = EmotePackageSet.from(entity.emoteType)
                )
            }
        }
    }
}