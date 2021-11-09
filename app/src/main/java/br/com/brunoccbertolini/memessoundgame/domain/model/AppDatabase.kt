package br.com.brunoccbertolini.memessoundgame.domain.model

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database (entities = [MemeEntity::class], version = 1)
abstract class AppDatabase: RoomDatabase() {

abstract val memeDao: MemeDao

    companion object{
        @Volatile
        private var INSTANCE: AppDatabase? = null


        fun getInstance(context: Context): AppDatabase {
            synchronized(this){
                var instance: AppDatabase? = INSTANCE
                if(instance == null){
                    instance = Room.databaseBuilder(
                        context,
                        AppDatabase::class.java,
                        "app.db")
                        .allowMainThreadQueries()
                        .createFromAsset("database/meme.db")
                        .build()
                }
                return instance
            }
        }
    }
}
