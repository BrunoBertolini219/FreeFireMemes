package br.com.brunoccbertolini.memessoundgame.data.repository

import br.com.brunoccbertolini.memessoundgame.domain.model.MemeEntity

interface MemeRepository {


    suspend fun upsertMeme(title: String, imgURL: String, audioUrl: String): Long

    suspend fun deleteMeme(id: Long)

    suspend fun deleteAllMemes()

    suspend fun getAllMemes(): List<MemeEntity>

    suspend fun getMeme(id: Long): MemeEntity

}