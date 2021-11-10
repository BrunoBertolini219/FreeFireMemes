package br.com.brunoccbertolini.memessoundgame.ui.memegallery

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.brunoccbertolini.memessoundgame.R
import br.com.brunoccbertolini.memessoundgame.domain.model.MemeEntity
import br.com.brunoccbertolini.memessoundgame.data.repository.MemeRepository
import br.com.brunoccbertolini.memessoundgame.domain.usecase.DeleteMemeUseCase
import br.com.brunoccbertolini.memessoundgame.domain.usecase.GetMemesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class MemeGalleryViewModel @Inject constructor(
    private val deleteMemeUseCase: DeleteMemeUseCase,
    private val getMemesUseCase: GetMemesUseCase
) : ViewModel() {
    private val _allMemesEvent = MutableLiveData<List<MemeEntity>>()
    val allMemeEvent: LiveData<List<MemeEntity>>
        get() = _allMemesEvent

    private val _memeRemoveEvent = MutableLiveData<Int>()
    val memeRemoveEvent: LiveData<Int>
        get() = _memeRemoveEvent

    fun deleteMeme(id: Long) = viewModelScope.launch {
      try {
          deleteMemeUseCase.invoke(id)
          getMemes()
          _memeRemoveEvent.value = R.string.meme_deleted_succesfully
      }catch (e: Exception){
          _memeRemoveEvent.value = R.string.error_deleting_meme
      }


        Log.i("TAG_DELETE_VIEWMODEL", "deleteMeme: $id")

    }

    fun getMemes() = viewModelScope.launch {
        _allMemesEvent.postValue(getMemesUseCase.invoke())
    }

    sealed class MemeState {
        object Deleted : MemeState()
    }

    companion object {
        private val TAG = MemeGalleryViewModel::class.java.simpleName
    }
}
