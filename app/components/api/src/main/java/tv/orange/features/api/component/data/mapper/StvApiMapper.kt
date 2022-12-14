package tv.orange.features.api.component.data.mapper

import tv.orange.models.abc.EmotePackageSet
import tv.orange.models.data.avatars.AvatarSet
import tv.orange.models.data.badges.BadgeBaseImpl
import tv.orange.models.data.badges.BadgeSet
import tv.orange.models.data.emotes.Emote
import tv.orange.models.data.emotes.impl.EmoteStvImpl
import tv.orange.models.retrofit.stv.BadgesData
import tv.orange.models.retrofit.stv.StvEmote
import javax.inject.Inject

class StvApiMapper @Inject constructor() {
    fun mapEmotes(emotes: List<StvEmote>, isChannelEmotes: Boolean): List<Emote> {
        return emotes.map { emote ->
            EmoteStvImpl(
                emoteId = emote.id,
                emoteCode = emote.name,
                animated = emote.mime == "image/gif" || emote.mime == "image/webp",
                packageSet = if (isChannelEmotes) {
                    EmotePackageSet.StvChannel
                } else {
                    EmotePackageSet.StvGlobal
                },
                isZeroWidth = emote.visibility_simple.contains("ZERO_WIDTH")
            )
        }
    }

    fun map(response: BadgesData): BadgeSet {
        val builder = BadgeSet.Builder()

        response.badges.forEach { badge ->
            getBadgeUrl(badge.urls)?.let { url ->
                val badgeImpl = BadgeBaseImpl(
                    code = "7tv",
                    url = url
                )
                badge.users.forEach { userIdString ->
                    userIdString.toIntOrNull()?.let { userId ->
                        builder.addBadge(
                            badge = badgeImpl,
                            userId = userId
                        )
                    }
                }
            }
        }

        return builder.build()
    }

    fun map(response: HashMap<String, String>): AvatarSet {
        return AvatarSet().apply {
            response.forEach {
                add(channelName = it.key.lowercase(), url = it.value)
            }
        }
    }

    companion object {
        private fun getBadgeUrl(urls: List<List<String>>): String? {
            if (urls.isEmpty()) {
                return null
            }

            return when (urls.size) {
                0 -> return null
                1 -> urls[0][1]
                else -> urls[1][1]
            }
        }
    }
}