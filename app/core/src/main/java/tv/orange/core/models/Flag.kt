package tv.orange.core.models

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
        ListValue(listOf("default", "core", "exo"), "default")
    ),
    DELETED_MESSAGES(
        "deleted_messages",
        "orange_settings_deleted_messages",
        ListValue(listOf("default", "mod", "strikethrough", "grey"), "default"),
    );

    companion object {
        fun Flag.valueBoolean(): Boolean {
            return getBoolean(this.value)
        }

        fun Flag.valueList(): List<String> {
            return getList(this.value)
        }

        fun Flag.valueInt(): Int {
            return getInt(this.value)
        }

        fun Flag.valueString(): String {
            return getString(this.value)
        }

        fun findByKey(key: String): Flag? {
            return EnumSet.allOf(Flag::class.java).firstOrNull {
                it.prefKey == key
            }
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
            if (holder is ListValue) {
                return holder.value
            }

            throw IllegalStateException("$holder")
        }

        fun getList(holder: ValueHolder): List<String> {
            if (holder is ListValue) {
                return holder.getVariants()
            }

            throw IllegalStateException("$holder")
        }
    }

    interface ValueHolder {
        val value: Any
        val type: Type
    }

    enum class Type {
        BOOLEAN, INTEGER, STRING, LIST, UNKNOWN
    }

    class ListValue(private val variants: List<String>, default: String) :
        ValueHolder {
        private var selected: String = set(default)

        fun set(value: String): String {
            if (!variants.contains(value)) {
                throw IllegalStateException("debug")
            }

            selected = value

            return selected
        }

        override val value: String
            get() = selected

        override val type: Type
            get() = Type.LIST

        fun getVariants(): List<String> {
            return variants
        }
    }

    class BooleanValue(bool: Any? = false) : ValueHolder {
        override var value = when (bool) {
            is Boolean -> bool
            is String -> bool.toBoolean()
            null -> false
            else -> bool.toString().toBoolean()
        }
        override val type: Type
            get() = Type.BOOLEAN
    }

    class IntegerValue(i: Any? = 0) : ValueHolder {
        override var value = when (i) {
            is Int -> i
            is String -> i.toInt()
            null -> 0
            else -> i.toString().toInt()
        }
        override val type: Type
            get() = Type.INTEGER
    }

    class StringValue(s: Any? = null) : ValueHolder {
        override var value = when (s) {
            is String -> s
            null -> ""
            else -> s.toString()
        }
        override val type: Type
            get() = Type.STRING
    }

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
}