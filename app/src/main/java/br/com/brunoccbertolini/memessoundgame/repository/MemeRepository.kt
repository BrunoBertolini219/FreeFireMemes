package br.com.brunoccbertolini.memessoundgame.repository

import android.graphics.Bitmap
import android.net.Uri
import androidx.lifecycle.LiveData
import br.com.brunoccbertolini.memessoundgame.model.MemeEntity

interface MemeRepository {


    suspend fun upsertMeme(title: String, imgURL: String, audioUrl: String): Long

    suspend fun deleteMeme(id: Long)

    suspend fun deleteAllMemes()

    suspend fun getAllMemes(): List<MemeEntity>

    suspend fun getMeme(id: Long): MemeEntity

}