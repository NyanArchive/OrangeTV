package tv.orange.core.models.flag.variants

import tv.orange.core.models.flag.Internal
import tv.orange.models.data.emotes.Emote

enum class EmoteQuality(val value: String) : Internal.Variant {
    LOW("low"),
    MEDIUM("medium"),
    LARGE("large");

    override fun getVariants(): List<Internal.Variant> {
        return values().toList()
    }

    override fun getDefault(): Internal.Variant {
        return MEDIUM
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

    fun toSize(): Emote.Size {
        return when(this) {
            LOW -> Emote.Size.SMALL
            MEDIUM -> Emote.Size.MEDIUM
            LARGE -> Emote.Size.LARGE
        }
    }
}