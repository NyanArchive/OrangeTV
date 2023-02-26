package tv.orange.features.api.component.data.mapper

import tv.orange.core.LoggerImpl
import tv.orange.models.abc.EmotePackageSet
import tv.orange.models.data.avatars.AvatarSet
import tv.orange.models.data.badges.BadgeBaseImpl
import tv.orange.models.data.badges.BadgeSet
import tv.orange.models.data.emotes.Emote
import tv.orange.models.data.emotes.impl.EmoteStvV3Impl
import tv.orange.models.retrofit.stv.BadgesData
import tv.orange.models.retrofit.stv.v3.EmoteLifecycle
import tv.orange.models.retrofit.stv.v3.EmoteSet
import tv.orange.models.retrofit.stv.v3.ImageFormatModel
import tv.orange.models.retrofit.stv.v3.ImageHost
import java.math.BigInteger
import javax.inject.Inject

class StvApiMapper @Inject constructor() {
    fun mapEmotes(set: EmoteSet, isChannelEmotes: Boolean): List<Emote> {
        return set.emotes.mapNotNull { emote ->
            emote.data?.let { emotePartial ->
                if (emotePartial.lifecycle != EmoteLifecycle.Live) {
                    LoggerImpl.devDebug("Skip emote (lifecycle): $emotePartial")
                    return@mapNotNull null
                }
                if (!emotePartial.flags.isAllowedOnTwitch()) {
                    LoggerImpl.devDebug("Skip emote (disallowed): $emotePartial")
                    return@mapNotNull null
                }

                emotePartial.host.toSize()?.let { sizes ->
                    EmoteStvV3Impl(
                        emoteId = emote.id,
                        emoteCode = emote.name,
                        animated = emotePartial.animated,
                        packageSet = if (isChannelEmotes) {
                            EmotePackageSet.StvChannel
                        } else {
                            EmotePackageSet.StvGlobal
                        },
                        isZeroWidth = emotePartial.flags.isZeroWidthEmote(),
                        baseUrl = emotePartial.host.formatUrl(),
                        sizes = sizes
                    )
                }
            }
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

        private fun Int.isZeroWidthEmote(): Boolean {
            return BigInteger.valueOf(toLong()).testBit(8)
        }

        private fun Int.isAllowedOnTwitch(): Boolean {
            return !BigInteger.valueOf(toLong()).testBit(24)
        }

        private fun pickBestFormat(files: List<ImageFormatModel>): List<ImageFormatModel> {
            val entries = files.groupBy { it.format }
            entries[ImageFormatModel.ImageFormat.WEBP]?.let {
                return it
            }
            entries[ImageFormatModel.ImageFormat.GIF]?.let {
                return it
            }
            entries[ImageFormatModel.ImageFormat.PNG]?.let {
                return it
            }

            return emptyList()
        }

        private fun ImageHost.formatUrl(): String {
            return url.let { url ->
                if (url.startsWith("//")) {
                    "https:$url"
                } else {
                    url
                }
            }.let { url ->
                if (url.endsWith("/")) {
                    url
                } else {
                    "$url/"
                }
            }
        }

        private fun ImageHost.toSize(): Triple<String, String?, String?>? {
            val pickedFiles = pickBestFormat(files).sortedBy { it.width }

            var small: String? = null
            var medium: String? = null
            var large: String? = null

            var width = 0
            pickedFiles.forEach { file ->
                if (width == 0) {
                    width = file.width
                    small = file.name
                } else {
                    when (file.width / width) {
                        1 -> small = file.name
                        2 -> medium = file.name
                        3 -> large = file.name
                        4 -> large = file.name
                    }
                }
            }

            small?.let { smallSize ->
                return Triple(smallSize, medium, large)
            }

            return null
        }
    }
}