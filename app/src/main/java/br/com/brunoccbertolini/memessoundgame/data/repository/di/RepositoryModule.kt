package br.com.brunoccbertolini.memessoundgame.data.repository.di

import br.com.brunoccbertolini.memessoundgame.data.repository.MemeRepository
import br.com.brunoccbertolini.memessoundgame.data.repository.MemeRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModule {

    @Singleton
    @Binds
    fun bindMemeRepository(memeRepository: MemeRepositoryImpl): MemeRepository
}