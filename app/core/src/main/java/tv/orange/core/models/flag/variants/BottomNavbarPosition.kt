package tv.orange.core.models.flag.variants

import tv.orange.core.models.flag.core.Variant

enum class BottomNavbarPosition(val value: String) : Variant {
    Default("default"),
    Top("top"),
    Hidden("hidden");

    override fun getDefault(): Variant {
        return Default
    }

    override fun toString(): String {
        return value
    }
}