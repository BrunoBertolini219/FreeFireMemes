package br.com.brunoccbertolini.memessoundgame.domain.model

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [MemeEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun getMemeDao(): MemeDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null
        private val LOCK = Any()


        operator fun invoke(context: Context) = INSTANCE ?: synchronized(LOCK) {
            INSTANCE ?: createDatabase(context).also { INSTANCE = it }
        }

        private fun createDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "meme.db"
            )
                .allowMainThreadQueries()
                .createFromAsset("database/meme.db")
                .build()
    }
}
