package br.com.brunoccbertolini.memessoundgame.domain.model.di

import android.content.Context
import androidx.room.Room
import br.com.brunoccbertolini.memessoundgame.domain.model.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ModelModule {

    @Singleton
    @Provides
    fun provideMemeDatabase(
        @ApplicationContext context: Context
    ) = Room.databaseBuilder(context, AppDatabase::class.java, "meme_db")

    @Singleton
    @Provides
    fun provideMemeDao(
        database: AppDatabase
    ) = database.memeDao
}