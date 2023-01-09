package tv.orange.core.models.flag.variants

import tv.orange.core.models.flag.core.Variant
import tv.orange.models.data.emotes.Emote

enum class EmoteQuality(val value: String) : Variant {
    LOW("low"),
    MEDIUM("medium"),
    LARGE("large");

    override fun getDefault(): Variant {
        return MEDIUM
    }

    override fun toString(): String {
        return value
    }

    fun toSize(): Emote.Size {
        return when(this) {
            LOW -> Emote.Size.SMALL
            MEDIUM -> Emote.Size.MEDIUM
            LARGE -> Emote.Size.LARGE
        }
    }
}