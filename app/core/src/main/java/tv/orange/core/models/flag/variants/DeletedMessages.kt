package tv.orange.core.models.flag.variants

import tv.orange.core.models.flag.core.Variant

enum class DeletedMessages(val value: String) : Variant {
    Default("default"),
    Mod("mod"),
    Strikethrough("strikethrough"),
    Grey("grey");

    override fun getDefault(): Variant {
        return Default
    }

    override fun toString(): String {
        return value
    }
}