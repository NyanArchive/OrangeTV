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
        items = listOf(
            Flag.BTTV_EMOTES,
            Flag.FFZ_EMOTES,
            Flag.STV_EMOTES,
            Flag.ITZ_EMOTES,
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
            Flag.LOCAL_LOGS,
            Flag.BYPASS_CHAT_BAN,
            Flag.HIDE_CHAT_HEADER,
            Flag.PINNED_MESSAGE,
            Flag.CHAT_SETTINGS
        )
    ),
    Player(
        destination = SettingsDestination.OrangePlayer,
        title = "orange_settings_menu_player",
        items = listOf(
            Flag.DISABLE_FAST_BREAD,
            Flag.COMPACT_PLAYER_FOLLOW_VIEW,
            Flag.PLAYER_IMPL,
            Flag.FORWARD_SEEK,
            Flag.REWIND_SEEK,
            Flag.MINI_PLAYER_SIZE,
            Flag.VODHUNTER,
            Flag.DISABLE_THEATRE_AUTOPLAY,
            Flag.SHOW_STATS_BUTTON,
            Flag.Proxy,
            Flag.IMPROVED_BACKGROUND_AUDIO,
            Flag.HIDE_UNFOLLOW_BUTTON
        )
    ),
    Gestures(
        destination = SettingsDestination.OrangeGestures,
        title = "orange_settings_menu_gestures",
        items = listOf(
            Flag.VOLUME_GESTURE,
            Flag.BRIGHTNESS_GESTURE
        )
    ),
    View(
        destination = SettingsDestination.OrangeView,
        title = "orange_settings_menu_view",
        items = listOf(
            Flag.CLIPFINITY,
            Flag.FOLLOWED_FULL_CARDS,
            Flag.HIDE_DISCOVER_TAB,
            Flag.BOTTOM_NAVBAR_POSITION,
            Flag.HIDE_GAME_SECTION,
            Flag.HIDE_RECOMMENDATION_SECTION,
            Flag.HIDE_RESUME_WATCHING_SECTION,
            Flag.HIDE_OFFLINE_CHANNEL_SECTION,
            Flag.SHOW_TIMER_BUTTON,
            Flag.SHOW_REFRESH_BUTTON,
            Flag.HIDE_CREATE_BUTTON
        )
    ),
    Patches(
        destination = SettingsDestination.OrangePatches,
        title = "orange_settings_menu_patches"
    ),
    Dev(
        destination = SettingsDestination.OrangeDev,
        title = "orange_settings_menu_dev",
        items = listOf(
            Flag.DEV_MODE,
            Flag.OKHTTP_LOGGING,
            Flag.FORCE_OTA
        )
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
        title = "orange_settings_menu_ota",
        items = listOf(
            Flag.UPDATER
        )
    ),
    Info(
        destination = SettingsDestination.OrangeInfo,
        title = "orange_settings_menu_info"
    );
}