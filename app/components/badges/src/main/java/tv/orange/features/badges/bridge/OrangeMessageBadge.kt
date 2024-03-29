package tv.orange.features.badges.bridge

import tv.twitch.android.models.chat.MessageBadge

open class OrangeMessageBadge(
    val badgeName: String,
    val badgeUrl: String,
    val badgeBackgroundColor: Int
) : MessageBadge(badgeName, "1")