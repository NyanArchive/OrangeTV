package tv.orange.core.models.flag

import tv.orange.core.models.flag.Internal.*
import tv.orange.core.models.flag.variants.DeletedMessages
import tv.orange.core.models.flag.variants.PlayerImpl
import java.util.*

enum class Flag(
    val prefKey: String,
    val titleRes: String?,
    val summaryRes: String?,
    val default: ValueHolder,
    var value: ValueHolder
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
    );

    constructor(key: String, default: ValueHolder) : this(
        prefKey = key,
        titleRes = null,
        summaryRes = null,
        default = default,
        value = default
    )

    constructor(prefKey: String, titleRes: String, default: ValueHolder) : this(
        prefKey = prefKey,
        titleRes = titleRes,
        summaryRes = titleRes + "_desc",
        default = default,
        value = default
    )

    companion object {
        fun Flag.asBoolean(): Boolean {
            return getBoolean(this.value)
        }

        fun <T : Variant> Flag.asVariant(): T {
            return getVariant(this.value)
        }

        fun Flag.asInt(): Int {
            return getInt(this.value)
        }

        fun Flag.asString(): String {
            return getString(this.value)
        }

        fun getInt(holder: ValueHolder): Int {
            if (holder is IntegerValue) {
                return holder.value
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

        fun findByKey(key: String): Flag? {
            return EnumSet.allOf(Flag::class.java).firstOrNull {
                it.prefKey == key
            }
        }
    }
}