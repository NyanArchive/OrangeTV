package tv.orange.features.chat.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "emotes")
data class FavEmoteEntity(
    @PrimaryKey(autoGenerate = true) var uid: Int = 0,
    val isTwitchEmote: Boolean,
    val isGlobalEmote: Boolean,
    val emoteId: String?,
    val emoteCode: String,
    val channelId: String,
    val emoteType: String,
    val isAnimated: Boolean
)