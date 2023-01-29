package tv.orange.features.api.component.data.mapper

import tv.orange.models.abc.EmotePackageSet
import tv.orange.models.data.badges.BadgeBaseImpl
import tv.orange.models.data.badges.BadgeSet
import tv.orange.models.data.emotes.Emote
import tv.orange.models.data.emotes.impl.EmoteItzImpl
import tv.orange.models.retrofit.itz.ItzBadgesResponseData
import tv.orange.models.retrofit.itz.ItzEmote
import tv.orange.models.retrofit.itz.ItzResponseData
import javax.inject.Inject

class ItzApiMapper @Inject constructor() {
    fun mapEmotes(response: ItzResponseData): Pair<List<Emote>, HashMap<Int, MutableList<Emote>>> {
        val channelEmotes = hashMapOf<Int, MutableList<Emote>>()

        response.data.channel_emotes.forEach { entry ->
            entry.key.toIntOrNull()?.let { channelId ->
                if (!channelEmotes.containsKey(channelId)) {
                    channelEmotes[channelId] = mutableListOf()
                }
                channelEmotes[channelId]?.addAll(entry.value.emotes.map { map(it, false) })
            }
        }

        val globalEmotes = response.data.global_emotes.map { map(it, true) }

        return globalEmotes to channelEmotes
    }

    fun mapBadges(data: ItzBadgesResponseData): BadgeSet {
        return BadgeSet.Builder().apply {
            data.badges.forEach { badge ->
                badge.users.forEach { userId ->
                    if (userId.isNotBlank()) {
                        userId.toIntOrNull()?.let { userIntId ->
                            addBadge(
                                badge = BadgeBaseImpl(
                                    code = "homies",
                                    url = badge.image2
                                ),
                                userId = userIntId
                            )
                        }

                    }
                }
            }
        }.build()
    }

    companion object {
        private fun map(emote: ItzEmote, isGlobal: Boolean): Emote {
            return EmoteItzImpl(
                emoteId = emote.id,
                emoteCode = emote.name,
                packageSet = if (isGlobal) {
                    EmotePackageSet.ItzGlobal
                } else {
                    EmotePackageSet.ItzChannel
                }
            )
        }
    }
}