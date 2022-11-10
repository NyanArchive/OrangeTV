package tv.orange.core.models.flag.variants

import tv.orange.core.models.flag.Internal

enum class UpdateChannel(val value: String) : Internal.Variant {
    Disabled("disabled"),
    Release("release"),
    Beta("beta"),
    Dev("dev");

    override fun getVariants(): List<Internal.Variant> {
        return values().toList()
    }

    override fun getDefault(): Internal.Variant {
        return Dev
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