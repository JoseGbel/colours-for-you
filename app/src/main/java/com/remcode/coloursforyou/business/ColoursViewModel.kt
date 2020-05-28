package com.remcode.coloursforyou.business

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.remcode.coloursforyou.data.repository.MainRepositoryImpl
import com.remcode.coloursforyou.utils.toHexColourString
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random

class ColoursViewModel : ViewModel() {

    private val _playSplatFx = MutableLiveData<Boolean>(false)
    val playSplatFx: LiveData<Boolean>
        get() = _playSplatFx

    private val _word = MutableLiveData<List<String>>()
    val word : LiveData<List<String>>
        get() = _word

    private val _loading = MutableLiveData<Boolean>(true)
    val loading : LiveData<Boolean>
        get() = _loading

    fun getRandomHexColour(): String? {
        return Random.nextInt(0, 16777215).toHexColourString()
    }

    fun getRandomWord() {
        _loading.postValue(true)
        val job = viewModelScope.launch {
            delay(2000)
            _word.postValue(MainRepositoryImpl().getRandomWord())
        }

        job.invokeOnCompletion {
            _loading.postValue(false)
            _playSplatFx.postValue(true)
        }
    }
}
