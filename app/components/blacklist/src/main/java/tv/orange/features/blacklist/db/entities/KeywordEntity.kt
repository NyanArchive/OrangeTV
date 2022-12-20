package tv.orange.features.blacklist.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "keywords")
data class KeywordEntity(
    @PrimaryKey(autoGenerate = true) var uid: Int = 0,
    val word: String,
    var type: String
)
