package tv.orange.core.models.flag.variants

import tv.orange.core.models.flag.core.Variant

enum class ProxyImpl(val value: String) : Variant {
    Disabled("disabled"),
    Twitching("twitching"),
    TTV_LOL("ttv_lol"),
    PURPLE("purple"),
    GAY("gay");

    override fun getDefault(): Variant {
        return Disabled
    }

    override fun toString(): String {
        return value
    }
}