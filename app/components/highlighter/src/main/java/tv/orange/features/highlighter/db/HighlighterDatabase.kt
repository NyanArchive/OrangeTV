package tv.orange.features.highlighter.db

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import tv.orange.features.highlighter.db.entities.KeywordEntity
import tv.twitch.android.app.core.ApplicationContext

@Database(entities = [KeywordEntity::class], version = 1, exportSchema = false)
abstract class HighlighterDatabase : RoomDatabase() {
    abstract fun highlighterDAO(): HighlighterDAO

    companion object {
        private const val DATABASE_NAME = "orange_highlighter_v8"

        val INSTANCE by lazy {
            Room.databaseBuilder(
                ApplicationContext.getInstance().getContext(),
                HighlighterDatabase::class.java, DATABASE_NAME
            ).build()
        }
    }
}