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
        "mod_settings_dev_mode",
        BooleanValue(false)
    ),
    BTTV_EMOTES(
        "bttv_emotes",
        "mod_settings_bttv_emotes",
        BooleanValue(true)
    ),
    FFZ_EMOTES(
        "ffz_emotes",
        "mod_settings_ffz_emotes",
        BooleanValue(true)
    ),
    STV_EMOTES(
        "stv_emotes",
        "mod_settings_stv_emotes",
        BooleanValue(true)
    ),
    FFZ_BADGES(
        "ffz_badges",
        "mod_settings_ffz_badges",
        BooleanValue(true)
    ),
    STV_BADGES(
        "stv_badges",
        "mod_settings_stv_badges",
        BooleanValue(true)
    ),
    CHE_BADGES(
        "che_badges",
        "mod_settings_che_badges",
        BooleanValue(true)
    ),
    CHA_BADGES(
        "cha_badges",
        "mod_settings_cha_badges",
        BooleanValue(true)
    );

    companion object {
        fun Flag.valueBoolean(): Boolean {
            return getBoolean(this.value)
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

        fun findByName(name: String): Flag? {
            return EnumSet.allOf(Flag::class.java).firstOrNull {
                it.name == name
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

            throw IllegalStateException("$holder")
        }
    }

    interface ValueHolder {
        val value: Any
        val type: Type
    }

    enum class Type {
        BOOLEAN, INTEGER, STRING, UNKNOWN
    }

    class BooleanValue(bool: Any?) : ValueHolder {
        constructor() : this(null)

        override var value = when (bool) {
            is Boolean -> bool
            is String -> bool.toBoolean()
            null -> false
            else -> bool.toString().toBoolean()
        }
        override val type: Type
            get() = Type.BOOLEAN
    }

    class IntegerValue(i: Any?) : ValueHolder {
        constructor() : this(null)

        override var value = when (i) {
            is Int -> i
            is String -> i.toInt()
            null -> 0
            else -> i.toString().toInt()
        }
        override val type: Type
            get() = Type.INTEGER
    }

    class StringValue(s: Any?) : ValueHolder {
        constructor() : this(null)

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