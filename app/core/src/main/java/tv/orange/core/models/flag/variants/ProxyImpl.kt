package tv.orange.core.models.flag.variants

import tv.orange.core.models.flag.core.Variant

enum class ProxyImpl(val value: String) : Variant {
    Disabled("disabled"),
    Twitching("twitching"),
    TTSFTP("ttsftp"),
    TTV_LOL("ttv_lol"),
    PURPLE("purple");

    override fun getVariants(): List<Variant> {
        return values().toList()
    }

    override fun getDefault(): Variant {
        return Disabled
    }

    override fun fromString(str: String): Variant? {
        return values().firstOrNull { it.value == str }
    }

    override fun toString(): String {
        return value
    }

    override fun isDefault(): Boolean {
        return this == getDefault()
    }
}