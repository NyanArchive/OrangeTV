package tv.orange.core.models.flag

import tv.orange.core.models.flag.Internal.*
import tv.orange.core.models.flag.variants.*
import java.util.*

enum class Flag(
    val preferenceKey: String,
    val titleResName: String?,
    val summaryResName: String?,
    val defaultHolder: ValueHolder,
    var valueHolder: ValueHolder
) {
    DEV_MODE(
        "dev_mode",
        "orange_settings_dev_mode",
        BooleanValue()
    ),
    CHAT_TIMESTAMPS(
        "chat_timestamps",
        "orange_settings_chat_timestamps",
        BooleanValue()
    ),
    DISABLE_LINK_DISCLAIMER(
        "disable_link_disclaimer",
        "orange_settings_disable_link_disclaimer",
        BooleanValue()
    ),
    HIDE_LEADERBOARDS(
        "hide_leaderboards",
        "orange_settings_hide_leaderboards",
        BooleanValue()
    ),
    DISABLE_FAST_BREAD(
        "disable_fast_bread",
        "orange_settings_disable_fast_bread",
        BooleanValue()
    ),
    FOLLOWED_FULL_CARDS(
        "followed_full_cards",
        "orange_settings_followed_full_cards",
        BooleanValue()
    ),
    DISABLE_HYPE_TRAIN(
        "disable_hype_train",
        "orange_settings_disable_hype_train",
        BooleanValue()
    ),
    HIDE_DISCOVER_TAB(
        "hide_discover_tab",
        "orange_settings_hide_discover_tab",
        BooleanValue()
    ),
    VIBRATE_ON_MENTION(
        "vibrate_on_mention",
        "orange_settings_vibrate_on_mention",
        BooleanValue()
    ),
    HIDE_TOP_CHAT_PANEL_VODS(
        "hide_top_chat_panel_vods",
        "orange_settings_hide_top_chat_panel_vods",
        BooleanValue()
    ),
    PRONOUNS(
        "pronouns",
        "orange_settings_pronouns",
        BooleanValue()
    ),
    AUTO_HIDE_MESSAGE_INPUT(
        "auto_hide_message_input",
        "orange_settings_auto_hide_message_input",
        BooleanValue()
    ),
    COMPACT_PLAYER_FOLLOW_VIEW(
        "compact_player_follow_view",
        "orange_settings_compact_player_follow_view",
        BooleanValue()
    ),
    BTTV_EMOTES(
        "bttv_emotes",
        "orange_settings_bttv_emotes",
        BooleanValue(true)
    ),
    FFZ_EMOTES(
        "ffz_emotes",
        "orange_settings_ffz_emotes",
        BooleanValue(true)
    ),
    STV_EMOTES(
        "stv_emotes",
        "orange_settings_stv_emotes",
        BooleanValue(true)
    ),
    FFZ_BADGES(
        "ffz_badges",
        "orange_settings_ffz_badges",
        BooleanValue(true)
    ),
    STV_BADGES(
        "stv_badges",
        "orange_settings_stv_badges",
        BooleanValue(true)
    ),
    CHE_BADGES(
        "che_badges",
        "orange_settings_che_badges",
        BooleanValue(true)
    ),
    CHA_BADGES(
        "cha_badges",
        "orange_settings_cha_badges",
        BooleanValue(true)
    ),
    STV_AVATARS(
        "stv_avatars",
        "orange_settings_stv_avatars",
        BooleanValue(true)
    ),
    CHAT_HISTORY(
        "chat_history",
        "orange_settings_chat_history",
        BooleanValue(true)
    ),
    DISABLE_STICKY_HEADERS_EP(
        "disable_sticky_headers_ep",
        "orange_settings_disable_sticky_headers_ep",
        BooleanValue()
    ),
    HIDE_BITS_BUTTON(
        "hide_bits_button",
        "orange_settings_hide_bits_button",
        BooleanValue()
    ),
    PLAYER_IMPL(
        "player_impl",
        "orange_settings_player_impl",
        ListValue(PlayerImpl.Default)
    ),
    DELETED_MESSAGES(
        "deleted_messages",
        "orange_settings_deleted_messages",
        ListValue(DeletedMessages.Default),
    ),
    BOTTOM_NAVBAR_POSITION(
        "bottom_navbar_position",
        "orange_settings_bottom_navbar_position",
        ListValue(BottomNavbarPosition.Default),
    ),
    CHAT_FONT_SIZE(// ++++++++++++++++++++++++++++++++++++++++++++
        "chat_font_size",
        "orange_settings_chat_font_size",
        ListValue(FontSize.SP13),
    ),
    EMOTE_QUALITY(// ++++++++++++++++++++++++++++++++++++++++++++
        "emote_quality",
        "orange_settings_emote_quality",
        ListValue(EmoteQuality.MEDIUM),
    ),
    LANDSCAPE_CHAT_SIZE(// ++++++++++++++++++++++++++++++++++++++++++++
        "landscape_chat_size",
        "orange_settings_landscape_chat_size",
        IntegerRangeValue(10, 50, 30),
    ),
    FORWARD_SEEK(
        "forward_seek",
        "orange_settings_forward_seek",
        IntegerRangeValue(5, 120, 30, step = 5),
    ),
    REWIND_SEEK(
        "backward_seek",
        "orange_settings_rewind_seek",
        IntegerRangeValue(5, 120, 10, step = 5),
    ),
    MINI_PLAYER_SIZE(
        "mini_player_size",
        "orange_settings_mini_player_size",
        IntegerRangeValue(50, 200, 100, step = 5),
    ),
    VIBRATION_DURATION(// ++++++++++++++++++++++++++++++++++++++++++++
        "vibration_duration",
        "orange_settings_vibration_duration",
        IntegerRangeValue(10, 1000, 100, step = 10),
    ),
    LANDSCAPE_CHAT_OPACITY(// ++++++++++++++++++++++++++++++++++++++++++++
        "landscape_chat_opacity",
        "orange_settings_landscape_chat_opacity",
        IntegerRangeValue(0, 100, 30),
    );

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
        fun Flag.asBoolean(): Boolean {
            return getBoolean(holder = this.valueHolder)
        }

        fun Flag.asIntRange(): IntegerRangeValue {
            return getIntegerRange(holder = this.valueHolder)
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
            if (holder is IntegerValue) {
                return holder.value
            }
            if (holder is IntegerRangeValue) {
                return holder.getCurrentValue()
            }

            throw IllegalStateException("$holder")
        }

        fun getBoolean(holder: ValueHolder): Boolean {
            if (holder is BooleanValue) {
                return holder.value
            }

            throw IllegalStateException("$holder")
        }

        fun getString(holder: ValueHolder): String {
            if (holder is StringValue) {
                return holder.value
            }
            if (holder is ListValue<*>) {
                return holder.value
            }

            throw IllegalStateException("$holder")
        }

        fun <T : Variant> getVariant(holder: ValueHolder): T {
            if (holder is ListValue<*>) {
                return holder.getVariant()
            }

            throw IllegalStateException("$holder")
        }

        fun getIntegerRange(holder: ValueHolder): IntegerRangeValue {
            if (holder is IntegerRangeValue) {
                return holder
            }

            throw IllegalStateException("$holder")
        }

        fun findByKey(prefKey: String): Flag? {
            return EnumSet.allOf(Flag::class.java).firstOrNull {
                it.preferenceKey == prefKey
            }
        }
    }
}