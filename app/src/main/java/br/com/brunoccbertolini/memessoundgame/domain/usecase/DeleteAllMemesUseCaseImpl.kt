package br.com.brunoccbertolini.memessoundgame.domain.usecase

import br.com.brunoccbertolini.memessoundgame.data.repository.MemeRepository
import java.lang.Exception
import javax.inject.Inject

class DeleteAllMemesUseCaseImpl @Inject constructor(
    private val repository: MemeRepository
): DeleteAllMemesUseCase{

    override suspend fun invoke() {
        try {
            repository.deleteAllMemes()
        }catch (e: Exception){
            throw e
        }
    }
}