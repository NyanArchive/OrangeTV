package tv.orange.core.models.flag

import android.graphics.Color
import tv.orange.core.PreferencesManagerCore
import tv.orange.core.models.flag.core.*
import tv.orange.core.models.flag.variants.*
import java.util.*

enum class Flag(
    val preferenceKey: String,
    val titleResName: String?,
    val summaryResName: String?,
    val defaultHolder: ValueHolder,
    var valueHolder: ValueHolder
) {
    // BOOL
    BTTV_EMOTES("bttv_emotes", "orange_settings_bttv_emotes", BooleanValue(true)),
    FFZ_EMOTES("ffz_emotes", "orange_settings_ffz_emotes", BooleanValue(true)),
    STV_EMOTES("stv_emotes", "orange_settings_stv_emotes", BooleanValue(true)),
    ITZ_EMOTES("itz_emotes", "orange_settings_itz_emotes", BooleanValue(false)),
    FFZ_BADGES("ffz_badges", "orange_settings_ffz_badges", BooleanValue(true)),
    STV_BADGES("stv_badges", "orange_settings_stv_badges", BooleanValue(true)),
    CHE_BADGES("che_badges", "orange_settings_che_badges", BooleanValue(true)),
    CHA_BADGES("cha_badges", "orange_settings_cha_badges", BooleanValue(true)),
    STV_AVATARS("stv_avatars", "orange_settings_stv_avatars", BooleanValue(true)),
    CHAT_HISTORY("chat_history", "orange_settings_chat_history", BooleanValue(true)),
    SHOW_TIMER_BUTTON("show_timer_button", "orange_settings_show_timer_button", BooleanValue(true)),
    SHOW_REFRESH_BUTTON("show_refresh_button", "orange_settings_show_refresh_button", BooleanValue(true)),
    SHOW_STATS_BUTTON("show_stats_button", "orange_settings_show_stats_button", BooleanValue(true)),
    CHAT_SETTINGS("chat_settings", "orange_settings_chat_settings", BooleanValue(true)),
    DEV_MODE("dev_mode", "orange_settings_dev_mode", BooleanValue()),
    CHAT_TIMESTAMPS("chat_timestamps", "orange_settings_chat_timestamps", BooleanValue()),
    DISABLE_LINK_DISCLAIMER("disable_link_disclaimer", "orange_settings_disable_link_disclaimer", BooleanValue()),
    HIDE_LEADERBOARDS("hide_leaderboards", "orange_settings_hide_leaderboards", BooleanValue()),
    DISABLE_FAST_BREAD("disable_fast_bread", "orange_settings_disable_fast_bread", BooleanValue()),
    FOLLOWED_FULL_CARDS("followed_full_cards", "orange_settings_followed_full_cards", BooleanValue()),
    DISABLE_HYPE_TRAIN("disable_hype_train", "orange_settings_disable_hype_train", BooleanValue()),
    HIDE_DISCOVER_TAB("hide_discover_tab", "orange_settings_hide_discover_tab", BooleanValue()),
    VIBRATE_ON_MENTION("vibrate_on_mention", "orange_settings_vibrate_on_mention", BooleanValue()),
    HIDE_TOP_CHAT_PANEL_VODS("hide_top_chat_panel_vods", "orange_settings_hide_top_chat_panel_vods", BooleanValue()),
    PRONOUNS("pronouns", "orange_settings_pronouns", BooleanValue()),
    AUTO_HIDE_MESSAGE_INPUT("auto_hide_message_input", "orange_settings_auto_hide_message_input", BooleanValue()),
    COMPACT_PLAYER_FOLLOW_VIEW("compact_player_follow_view", "orange_settings_compact_player_follow_view", BooleanValue()),
    BRIGHTNESS_GESTURE("brightness_gesture", "orange_settings_brightness_gesture", BooleanValue()),
    VOLUME_GESTURE("volume_gesture", "orange_settings_volume_gesture", BooleanValue()),
    DISABLE_THEATRE_AUTOPLAY("disable_theatre_autoplay", "orange_settings_disable_theatre_autoplay", BooleanValue()),
    DISABLE_STICKY_HEADERS_EP("disable_sticky_headers_ep", "orange_settings_disable_sticky_headers_ep", BooleanValue()),
    HIDE_BITS_BUTTON("hide_bits_button", "orange_settings_hide_bits_button", BooleanValue()),
    CLIPFINITY("clipfinity", "orange_settings_clipfinity", BooleanValue()),
    OKHTTP_LOGGING("okhttp_logging", "orange_settings_okhttp_logging", BooleanValue()),
    VODHUNTER("vodhunter", "orange_settings_vodhunter", BooleanValue()),
    BYPASS_CHAT_BAN("bypass_chat_ban", "orange_settings_bypass_chat_ban", BooleanValue()),
    HIDE_CHAT_HEADER("hide_chat_header", "orange_settings_hide_chat_header", BooleanValue()),
    HIDE_GAME_SECTION("hide_game_section", "orange_settings_hide_game_section", BooleanValue()),
    HIDE_RECOMMENDATION_SECTION("hide_recommendation_section", "orange_settings_hide_recommendation_section", BooleanValue()),
    HIDE_RESUME_WATCHING_SECTION("hide_resume_watching_section", "orange_settings_hide_resume_watching_section", BooleanValue()),
    HIDE_OFFLINE_CHANNEL_SECTION("hide_offline_channel_section", "orange_settings_hide_offline_channel_section", BooleanValue()),
    FORCE_OTA("force_ota", "orange_settings_force_ota", BooleanValue()),
    IMPROVED_BACKGROUND_AUDIO("improved_background_audio_v2", "orange_settings_improved_background_audio", BooleanValue()),
    HIDE_UNFOLLOW_BUTTON("hide_unfollow_button", "orange_settings_hide_unfollow_button", BooleanValue()),
    HIDE_CREATE_BUTTON("hide_create_button", "orange_settings_hide_create_button", BooleanValue()),
    HIDE_FSB("hide_fsb", "orange_settings_hide_fsb", BooleanValue()),
    FORCE_EXOPLAYER_FOR_VODS("force_exoplayer_for_vods", "orange_settings_force_exoplayer_for_vods", BooleanValue()),
    PREVENT_MOD_CLEAR("prevent_mod_clear", "orange_settings_prevent_mod_clear", BooleanValue()),
    FORCE_TOOLBAR_SEARCH_BUTTON("force_toolbar_search_button", "orange_settings_force_toolbar_search_button", BooleanValue()),
    HIDE_PLAYER_CREATE_CLIP_BUTTON("hide_player_create_clip_button", "orange_settings_hide_player_create_clip_button", BooleanValue()),
    FORCE_DISABLE_SENTRY("disable_sentry", "orange_settings_disable_sentry", BooleanValue()),
    HIDE_PLAYER_LIVE_SHARE_BUTTON("hide_player_live_share_button", "orange_settings_hide_player_live_share_button", BooleanValue()),
    HIDE_MESSAGE_INPUT("hide_message_input", "orange_settings_hide_message_input", BooleanValue()),
    // LIST
    PLAYER_IMPL("player_impl", "orange_settings_player_impl", ListValue(PlayerImpl::class)),
    DELETED_MESSAGES("deleted_messages", "orange_settings_deleted_messages", ListValue(DeletedMessages::class)),
    BOTTOM_NAVBAR_POSITION("bottom_navbar_position", "orange_settings_bottom_navbar_position", ListValue(BottomNavbarPosition::class)),
    CHAT_FONT_SIZE("chat_font_size", "orange_settings_chat_font_size", ListValue(FontSize::class)),
    EMOTE_QUALITY("emote_quality", "orange_settings_emote_quality", ListValue(EmoteQuality::class)),
    LOCAL_LOGS("local_logs", "orange_settings_local_logs", ListValue(LocalLogs::class)),
    UPDATER("updater_final", "orange_settings_updater_channel", ListValue(UpdateChannel::class)),
    PINNED_MESSAGE("pinned_message", "orange_settings_pinned_message", ListValue(PinnedMessageStrategy::class)),
    Proxy("proxy", "orange_settings_proxy", ListValue(ProxyImpl::class)),
    // INT
    USER_MENTION_COLOR("user_mention_color", "orange_settings_user_mention_color", IntValue(Color.argb(100, 255, 0, 0))),
    LANDSCAPE_CHAT_SIZE("landscape_chat_size", "orange_settings_landscape_chat_size", IntRangeValue(10, 50, 30)),
    LANDSCAPE_SPLIT_CHAT_SIZE("landscape_split_chat_size", "orange_settings_landscape_split_chat_size", IntRangeValue(10, 70, 50)),
    FORWARD_SEEK("forward_seek", "orange_settings_forward_seek", IntRangeValue(5, 120, 30, step = 5)),
    REWIND_SEEK("backward_seek", "orange_settings_rewind_seek", IntRangeValue(5, 120, 10, step = 5)),
    MINI_PLAYER_SIZE("mini_player_size", "orange_settings_mini_player_size", IntRangeValue(50, 200, 100, step = 5)),
    VIBRATION_DURATION("vibration_duration", "orange_settings_vibration_duration", IntRangeValue(10, 1000, 100, step = 10)),
    LANDSCAPE_CHAT_OPACITY("landscape_chat_opacity", "orange_settings_landscape_chat_opacity", IntRangeValue(0, 100, 30)),
    EXOPLAYER_VOD_SPEED("exoplayer_vod_speed", "orange_settings_exoplayer_vod_speed", IntRangeValue(25, 200, 100, step = 5));

    constructor(prefKey: String, value: ValueHolder) : this(
        preferenceKey = prefKey,
        titleResName = null,
        summaryResName = null,
        defaultHolder = value,
        valueHolder = value
    )

    constructor(prefKey: String, titleResName: String, value: ValueHolder) : this(
        preferenceKey = prefKey,
        titleResName = titleResName,
        summaryResName = titleResName + "_desc",
        defaultHolder = value,
        valueHolder = value
    )

    companion object {
        fun Flag.setValue(value: Any?) {
            valueHolder.setValue(value)
            save()
        }

        fun Flag.asBoolean(): Boolean {
            return getBoolean(holder = this.valueHolder)
        }

        fun Flag.asIntRange(): IntRangeValue {
            return getIntRange(holder = this.valueHolder)
        }

        fun <T : Variant> Flag.asVariant(): T {
            return getVariant(holder = this.valueHolder)
        }

        fun Flag.asInt(): Int {
            return getInt(holder = this.valueHolder)
        }

        fun Flag.asString(): String {
            return getString(holder = this.valueHolder)
        }

        fun getInt(holder: ValueHolder): Int {
            if (holder is IntValue) {
                return holder.getValue()
            }
            if (holder is IntRangeValue) {
                return holder.getValue()
            }

            throw IllegalStateException("$holder")
        }

        fun getBoolean(holder: ValueHolder): Boolean {
            if (holder is BooleanValue) {
                return holder.getValue()
            }

            throw IllegalStateException("$holder")
        }

        fun getString(holder: ValueHolder): String {
            if (holder is StringValue) {
                return holder.getValue()
            }
            if (holder is ListValue<*>) {
                return holder.getValue()
            }

            throw IllegalStateException("$holder")
        }

        @Suppress("UNCHECKED_CAST")
        fun <T : Variant> getVariant(holder: ValueHolder): T {
            if (holder is ListValue<*>) {
                return holder.getVariant() as T
            }

            throw IllegalStateException("$holder")
        }

        fun getIntRange(holder: ValueHolder): IntRangeValue {
            if (holder is IntRangeValue) {
                return holder
            }

            throw IllegalStateException("$holder")
        }

        fun findByKey(prefKey: String): Flag? {
            return EnumSet.allOf(Flag::class.java).firstOrNull {
                it.preferenceKey == prefKey
            }
        }

        fun Flag.save() {
            PreferencesManagerCore.saveToPreferences(this)
        }
    }
}