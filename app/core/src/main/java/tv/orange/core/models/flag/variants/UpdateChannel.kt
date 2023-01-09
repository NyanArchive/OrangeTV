package tv.orange.core.models.flag.variants

import tv.orange.core.models.flag.core.Variant

enum class UpdateChannel(val value: String) : Variant {
    Disabled("disabled"),
    Release("release"),
    Beta("beta"),
    Dev("dev");

    override fun getDefault(): Variant {
        return Release
    }

    override fun toString(): String {
        return value
    }
}