package br.com.brunoccbertolini.memessoundgame.view.memegallery

import android.util.Log
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.brunoccbertolini.memessoundgame.R
import br.com.brunoccbertolini.memessoundgame.model.MemeEntity
import br.com.brunoccbertolini.memessoundgame.repository.MemeRepository
import br.com.brunoccbertolini.memessoundgame.view.addmeme.AddMemeViewModel
import kotlinx.coroutines.launch
import java.lang.Exception


class MemeGalleryViewModel(
    private val repository: MemeRepository
) : ViewModel() {
    private val _allMemesEvent = MutableLiveData<List<MemeEntity>>()
    val allMemeEvent: LiveData<List<MemeEntity>>
        get() = _allMemesEvent

    private val _memeRemoveEvent = MutableLiveData<Int>()
    val memeRemoveEvent: LiveData<Int>
        get() = _memeRemoveEvent

    fun deleteMeme(id: Long) = viewModelScope.launch {
      try {
          repository.deleteMeme(id)
          getMemes()
          _memeRemoveEvent.value = R.string.meme_deleted_succesfully
      }catch (e: Exception){
          _memeRemoveEvent.value = R.string.error_deleting_meme
      }


        Log.i("TAG_DELETE_VIEWMODEL", "deleteMeme: $id")

    }

    fun getMemes() = viewModelScope.launch {
        _allMemesEvent.postValue(repository.getAllMemes())
    }

    sealed class MemeState {
        object Deleted : MemeState()
    }

    companion object {
        private val TAG = MemeGalleryViewModel::class.java.simpleName
    }
}
