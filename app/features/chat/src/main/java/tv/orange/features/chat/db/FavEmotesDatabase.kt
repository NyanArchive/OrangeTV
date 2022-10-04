package tv.orange.features.chat.db

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import tv.orange.features.chat.db.entities.FavEmoteEntity
import tv.twitch.android.app.core.ApplicationContext

@Database(entities = [FavEmoteEntity::class], version = 1, exportSchema = false)
abstract class FavEmotesDatabase : RoomDatabase() {
    abstract fun favEmotesDAO(): FavEmotesDAO

    companion object {
        private const val DATABASE_NAME = "orange_favemotes_v2"

        val INSTANCE by lazy {
            Room.databaseBuilder(
                ApplicationContext.getInstance().getContext(),
                FavEmotesDatabase::class.java, DATABASE_NAME
            ).build()
        }
    }
}