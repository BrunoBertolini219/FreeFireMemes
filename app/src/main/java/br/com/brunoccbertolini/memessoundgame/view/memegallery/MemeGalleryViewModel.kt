package br.com.brunoccbertolini.memessoundgame.view.memegallery

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.brunoccbertolini.memessoundgame.R
import br.com.brunoccbertolini.memessoundgame.model.MemeEntity
import br.com.brunoccbertolini.memessoundgame.repository.MemeRepository
import kotlinx.coroutines.launch

class MemeGalleryViewModel(
    private val repository: MemeRepository
) : ViewModel() {
    private val _allMemesEvent = MutableLiveData<List<MemeEntity>>()
    val allMemeEvent: LiveData<List<MemeEntity>>
        get() = _allMemesEvent

    fun getMemes() = viewModelScope.launch {
        _allMemesEvent.postValue(repository.getAllMemes())
    }
}
