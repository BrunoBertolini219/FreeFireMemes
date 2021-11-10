package br.com.brunoccbertolini.memessoundgame.domain.usecase

import br.com.brunoccbertolini.memessoundgame.data.repository.MemeRepositoryImpl
import br.com.brunoccbertolini.memessoundgame.domain.model.MemeEntity
import javax.inject.Inject

class GetMemeUseCaseImpl @Inject constructor(
    private val repository: MemeRepositoryImpl
): GetMemesUseCase {
    override suspend fun invoke(): List<MemeEntity> {
        return repository.getAllMemes()
    }

}