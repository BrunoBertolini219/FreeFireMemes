package br.com.brunoccbertolini.memessoundgame.domain.usecase

import br.com.brunoccbertolini.memessoundgame.data.repository.MemeRepositoryImpl
import java.lang.Exception
import javax.inject.Inject

class DeleteMemeUseCaseImpl @Inject constructor(
    private val repositoryImpl: MemeRepositoryImpl
): DeleteMemeUseCase {

    override suspend fun invoke(id: Long) {
        try {
            repositoryImpl.deleteMeme(id)
        }catch (e: Exception){
            throw e
        }
    }
}