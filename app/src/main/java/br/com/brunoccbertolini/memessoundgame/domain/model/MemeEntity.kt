package br.com.brunoccbertolini.memessoundgame.domain.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "meme")
@Parcelize
data class MemeEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String?,
    val imgUrl: String?,
    val audioUrl: String?
): Parcelable