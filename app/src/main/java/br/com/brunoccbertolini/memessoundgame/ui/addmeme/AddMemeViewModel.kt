package br.com.brunoccbertolini.memessoundgame.ui.addmeme

import android.content.Context
import android.os.Environment
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.brunoccbertolini.memessoundgame.R
import br.com.brunoccbertolini.memessoundgame.domain.usecase.CreateMemeUseCase
import br.com.brunoccbertolini.memessoundgame.domain.usecase.DeleteAllMemesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class AddMemeViewModel @Inject constructor(
    private val insertMemeUseCase: CreateMemeUseCase,
    private val deleteAllMemesUseCase: DeleteAllMemesUseCase
) : ViewModel() {


    private val _memeStateEventData = MutableLiveData<MemeState>()
    val memeStateEventData: LiveData<MemeState>
        get() = _memeStateEventData

    private val _messegeEventData = MutableLiveData<Int>()
    val messegeEventData: LiveData<Int>
        get() = _messegeEventData

    fun addMeme(title: String, imgURl: String, audioUrl: String) = viewModelScope.launch {
        try {
            insertMemeUseCase.invoke(title, imgURl, audioUrl)
            _memeStateEventData.value = MemeState.Inserted
            _messegeEventData.value = R.string.meme_inserted_succesfully

        } catch (ex: Exception) {
            _messegeEventData.value = R.string.error_inserting_meme
            Log.e(TAG, ex.toString())
        }
    }

    fun deleteAllMemes() = viewModelScope.launch {
        deleteAllMemesUseCase.invoke()
        _memeStateEventData.value = MemeState.Deleted
        _messegeEventData.value = R.string.meme_deleted_succesfully
    }

    fun getAppSpecificAlbumStorageDir(context: Context, albumName: String): File? {
        // Get the pictures directory that's inside the app-specific directory on
        // external storage.
        val file = File(
            context.getExternalFilesDir(
                Environment.DIRECTORY_PICTURES
            ), albumName
        )
        if (!file.mkdirs()) {
            Log.e(TAG, "Directory not created")
        }
        return file

    }

    sealed class MemeState {
        object Inserted : MemeState()
        object Deleted : MemeState()
    }

    companion object {
        private val TAG = AddMemeViewModel::class.java.simpleName
    }

}