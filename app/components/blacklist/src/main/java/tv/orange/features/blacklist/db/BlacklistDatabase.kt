package tv.orange.features.blacklist.db

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import tv.orange.features.blacklist.db.entities.KeywordEntity
import tv.twitch.android.app.core.ApplicationContext

@Database(entities = [KeywordEntity::class], version = 1, exportSchema = false)
abstract class BlacklistDatabase : RoomDatabase() {
    abstract fun highlighterDAO(): HighlighterDAO

    companion object {
        private const val DATABASE_NAME = "orange_blacklist"

        val INSTANCE by lazy {
            Room.databaseBuilder(
                ApplicationContext.getInstance().getContext(),
                BlacklistDatabase::class.java, DATABASE_NAME
            ).build()
        }
    }
}