package br.com.brunoccbertolini.memessoundgame.domain.usecase

interface DeleteMemeUseCase {

    suspend operator fun invoke(id: Long)
}