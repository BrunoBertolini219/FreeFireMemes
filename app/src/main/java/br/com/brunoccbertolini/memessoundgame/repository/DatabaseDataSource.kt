package br.com.brunoccbertolini.memessoundgame.repository


import br.com.brunoccbertolini.memessoundgame.model.MemeDao
import br.com.brunoccbertolini.memessoundgame.model.MemeEntity

class DatabaseDataSource (private val memeDao: MemeDao
): MemeRepository {
    override suspend fun upsertMeme(title: String, imgURL: String, audioUrl: String): Long {
        val meme = MemeEntity(
            title = title,
            imgUrl = imgURL,
            audioUrl = audioUrl
        )

        return memeDao.upsert(meme)
    }



    override suspend fun deleteMeme(id: Long) = memeDao.delete(id)

    override suspend fun deleteAllMemes() = memeDao.deleteAll()

    override suspend fun getAllMemes(): List<MemeEntity> = memeDao.getAll()

    override suspend fun getMeme(id: Long): MemeEntity = memeDao.getMeme(id)
}