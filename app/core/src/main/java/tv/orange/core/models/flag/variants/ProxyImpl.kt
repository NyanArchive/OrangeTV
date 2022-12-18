package tv.orange.core.models.flag.variants

import tv.orange.core.models.flag.Internal

enum class ProxyImpl(val value: String) : Internal.Variant {
    Disabled("disabled"),
    Twitching("twitching"),
    TTSFTP("ttsftp"),
    TTV_LOL("ttv_lol");

    override fun getVariants(): List<Internal.Variant> {
        return values().toList()
    }

    override fun getDefault(): Internal.Variant {
        return Disabled
    }

    override fun fromString(str: String): Internal.Variant? {
        return values().firstOrNull { it.value == str }
    }

    override fun toString(): String {
        return value
    }

    override fun isDefault(): Boolean {
        return this == getDefault()
    }
}