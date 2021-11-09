package br.com.brunoccbertolini.memessoundgame.domain.usecase.di

import br.com.brunoccbertolini.memessoundgame.domain.usecase.*
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface DomainModule {

    @Binds
    fun bindCreateMemeUseCase(useCase: CreateMemeUseCaseImpl ): CreateMemeUseCase

    @Binds
    fun bindGetMemesUseCase(useCase: GetMemeUseCaseImpl): GetMemesUseCase

    @Binds
    fun bindDeleteMemeUseCase(useCase: DeleteMemeUseCaseImpl): DeleteMemeUseCase

}