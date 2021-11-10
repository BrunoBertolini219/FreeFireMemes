package br.com.brunoccbertolini.memessoundgame.domain.usecase

import br.com.brunoccbertolini.memessoundgame.domain.model.MemeEntity

interface CreateMemeUseCase {

    suspend operator fun invoke(title: String, imageUrl: String, audio:String):Long

}