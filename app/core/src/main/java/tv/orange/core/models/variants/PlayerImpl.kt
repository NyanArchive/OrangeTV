package tv.orange.core.models.variants

import tv.orange.core.models.Flag

enum class PlayerImpl(val value: String) : Flag.Variant {
    Default("default"),
    Core("core"),
    Exo("exo");

    override fun getVariants(): List<Flag.Variant> {
        return values().toList()
    }

    override fun default(): Flag.Variant {
        return Default
    }

    override fun fromString(value: String): Flag.Variant {
        return values().firstOrNull { it.value == value }
            ?: throw IllegalStateException("Variant not found: $value")
    }

    override fun toString(): String {
        return value
    }
}