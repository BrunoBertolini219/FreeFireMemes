package br.com.brunoccbertolini.memessoundgame.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "meme")
data class MemeEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String?,
    val imgUrl: String?,
    val audioUrl: String?
) {

}