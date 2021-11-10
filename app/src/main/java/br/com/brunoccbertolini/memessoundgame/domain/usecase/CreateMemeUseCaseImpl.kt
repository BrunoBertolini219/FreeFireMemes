package br.com.brunoccbertolini.memessoundgame.domain.usecase

import br.com.brunoccbertolini.memessoundgame.data.repository.MemeRepository
import br.com.brunoccbertolini.memessoundgame.domain.usecase.CreateMemeUseCase
import javax.inject.Inject

class CreateMemeUseCaseImpl @Inject constructor(
    private val repository: MemeRepository
) : CreateMemeUseCase {
    override suspend fun invoke(title: String, imageUrl: String, audio: String): Long {
        return try {
            repository.upsertMeme(title, imageUrl, audio)
        } catch (e: Exception) {
            throw e
        }
    }

}