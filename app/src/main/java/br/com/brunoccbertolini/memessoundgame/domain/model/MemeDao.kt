package br.com.brunoccbertolini.memessoundgame.domain.model

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface MemeDao {


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(memeEntity: MemeEntity): Long

    @Query("DELETE FROM meme WHERE id= :id")
    suspend fun delete(id: Long)

    @Query("DELETE FROM meme")
    suspend fun deleteAll()

    @Query("SELECT * FROM meme")
    suspend fun getAll(): List<MemeEntity>

    @Query("SELECT * FROM meme WHERE id= :id")
    suspend fun getMeme(id: Long): MemeEntity

}