package tv.orange.features.api.component.data.mapper

import tv.orange.models.abs.EmotePackageSet
import tv.orange.models.data.avatars.AvatarSet
import tv.orange.models.data.badges.BadgeImpl
import tv.orange.models.data.badges.BadgeSet
import tv.orange.models.data.emotes.Emote
import tv.orange.models.data.emotes.EmoteImpl
import tv.orange.models.retrofit.stv.BadgesData
import tv.orange.models.retrofit.stv.StvEmote
import javax.inject.Inject

class StvApiMapper @Inject constructor() {
    fun mapEmotes(emotes: List<StvEmote>, isChannelEmote: Boolean): List<Emote> {
        return emotes.map { emote ->
            EmoteImpl(
                emoteCode = emote.name,
                animated = emote.mime == "image/gif" || emote.mime == "image/webp",
                smallUrl = getEmoteUrl("1x", emote.id),
                mediumUrl = getEmoteUrl("2x", emote.id),
                largeUrl = getEmoteUrl("4x", emote.id),
                packageSet = if (isChannelEmote) EmotePackageSet.StvChannel else EmotePackageSet.StvGlobal
            )
        }
    }

    fun mapBadges(badges: BadgesData): BadgeSet {
        val builder = BadgeSet.Builder()

        badges.badges.forEach { badge ->
            getBadgeUrl(badge.urls)?.let { url ->
                badge.users.forEach { userIdString ->
                    userIdString.toIntOrNull()?.let { userId ->
                        builder.addBadge(BadgeImpl(code = "7TV", url = url), userId)
                    }
                }
            }
        }

        return builder.build()
    }

    fun mapAvatars(map: HashMap<String, String>): AvatarSet {
        val avatarSet = AvatarSet()
        map.forEach {
            avatarSet.add(channelName = it.key.lowercase(), url = it.value)
        }

        return avatarSet
    }

    companion object {
        private const val STV_EMOTE_CDN = "https://cdn.7tv.app/emote/"

        private fun getEmoteUrl(size: String, emoteId: String): String {
            return "$STV_EMOTE_CDN$emoteId/$size"
        }

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