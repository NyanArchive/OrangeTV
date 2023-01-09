package tv.orange.core.models.flag.variants

import tv.orange.core.models.flag.core.Variant

enum class DeletedMessages(val value: String) : Variant {
    Default("default"),
    Mod("mod"),
    Strikethrough("strikethrough"),
    Grey("grey");

    override fun getVariants(): List<Variant> {
        return values().toList()
    }

    override fun getDefault(): Variant {
        return Default
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