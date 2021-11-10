package br.com.brunoccbertolini.memessoundgame.domain.usecase

import br.com.brunoccbertolini.memessoundgame.domain.model.MemeEntity

interface GetMemesUseCase {

    suspend operator fun invoke(): List<MemeEntity>

}