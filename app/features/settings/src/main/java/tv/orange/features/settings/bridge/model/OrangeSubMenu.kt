package tv.orange.features.settings.bridge.model

import tv.orange.core.models.flag.Flag
import tv.twitch.android.models.settings.SettingsDestination

enum class OrangeSubMenu(
    val destination: SettingsDestination,
    val title: String,
    val desc: String? = null,
    val items: Collection<Flag> = emptyList()
) {
    ThirdParty(
        destination = SettingsDestination.OrangeThirdParty,
        title = "orange_settings_menu_third_party",
        desc = null,
        items = listOf(
            Flag.BTTV_EMOTES,
            Flag.FFZ_EMOTES,
            Flag.STV_EMOTES,
            Flag.EMOTE_QUALITY,
            Flag.FFZ_BADGES,
            Flag.STV_BADGES,
            Flag.CHA_BADGES,
            Flag.CHE_BADGES,
            Flag.PRONOUNS,
            Flag.STV_AVATARS
        )
    ),
    Chat(
        destination = SettingsDestination.OrangeChat,
        title = "orange_settings_menu_chat",
        desc = null,
        items = listOf(
            Flag.CHAT_TIMESTAMPS,
            Flag.DISABLE_LINK_DISCLAIMER,
            Flag.HIDE_LEADERBOARDS,
            Flag.DISABLE_HYPE_TRAIN,
            Flag.HIDE_TOP_CHAT_PANEL_VODS,
            Flag.AUTO_HIDE_MESSAGE_INPUT,
            Flag.CHAT_HISTORY,
            Flag.DISABLE_STICKY_HEADERS_EP,
            Flag.HIDE_BITS_BUTTON,
            Flag.VIBRATE_ON_MENTION,
            Flag.VIBRATION_DURATION,
            Flag.DELETED_MESSAGES,
            Flag.CHAT_FONT_SIZE,
            Flag.LANDSCAPE_CHAT_SIZE,
            Flag.LANDSCAPE_CHAT_OPACITY,
            Flag.LOCAL_LOGS
        )
    ),
    Player(
        destination = SettingsDestination.OrangePlayer,
        title = "orange_settings_menu_player",
        desc = null,
        items = listOf(
            Flag.DISABLE_FAST_BREAD,
            Flag.COMPACT_PLAYER_FOLLOW_VIEW,
            Flag.PLAYER_IMPL,
            Flag.FORWARD_SEEK,
            Flag.REWIND_SEEK,
            Flag.MINI_PLAYER_SIZE
        )
    ),
    Gestures(
        destination = SettingsDestination.OrangeGestures,
        title = "orange_settings_menu_gestures"
    ),
    View(
        destination = SettingsDestination.OrangeView,
        title = "orange_settings_menu_view",
        desc = null,
        items = listOf(
            Flag.FOLLOWED_FULL_CARDS,
            Flag.HIDE_DISCOVER_TAB,
            Flag.BOTTOM_NAVBAR_POSITION
        )
    ),
    Patches(
        destination = SettingsDestination.OrangePatches,
        title = "orange_settings_menu_patches"
    ),
    Dev(
        destination = SettingsDestination.OrangeDev,
        title = "orange_settings_menu_dev",
        desc = null,
        items = listOf(Flag.DEV_MODE)
    ),
    Support(
        destination = SettingsDestination.OrangeSupport,
        title = "orange_settings_menu_support"
    ),
    Wiki(
        destination = SettingsDestination.OrangeWiki,
        title = "orange_settings_menu_wiki"
    ),
    OTA(
        destination = SettingsDestination.OrangeOTA,
        title = "orange_settings_menu_ota"
    ),
    Info(
        destination = SettingsDestination.OrangeInfo,
        title = "orange_settings_menu_info"
    );
}