package tv.orange.core.models.flag.variants

import tv.orange.core.models.flag.Internal

enum class BottomNavbarPosition(val value: String) : Internal.Variant {
    Default("default"),
    Top("top"),
    Hidden("hidden");

    override fun getVariants(): List<Internal.Variant> {
        return values().toList()
    }

    override fun default(): Internal.Variant {
        return Default
    }

    override fun fromString(value: String): Internal.Variant {
        return values().firstOrNull { it.value == value }
            ?: throw IllegalStateException("Variant not found: $value")
    }

    override fun toString(): String {
        return value
    }
}