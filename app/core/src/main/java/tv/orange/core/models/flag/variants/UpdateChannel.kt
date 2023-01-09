package tv.orange.core.models.flag.variants

import tv.orange.core.models.flag.core.Variant

enum class UpdateChannel(val value: String) : Variant {
    Disabled("disabled"),
    Release("release"),
    Beta("beta"),
    Dev("dev");

    override fun getVariants(): List<Variant> {
        return values().toList()
    }

    override fun getDefault(): Variant {
        return Release
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