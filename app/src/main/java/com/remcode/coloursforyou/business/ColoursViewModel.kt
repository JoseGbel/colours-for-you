package com.remcode.coloursforyou.business

import android.app.Application
import androidx.lifecycle.*
import com.remcode.coloursforyou.data.local.ColourDatabase
import com.remcode.coloursforyou.data.models.Colour
import com.remcode.coloursforyou.data.repository.MainRepositoryImpl
import com.remcode.coloursforyou.utils.toHexColourString
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random

class ColoursViewModel(application: Application) : AndroidViewModel(application) {

    private val _playSplatFx = MutableLiveData(false)
    val playSplatFx: LiveData<Boolean>
        get() = _playSplatFx

    private val _word = MutableLiveData<List<String>>()
    val word : LiveData<List<String>>
        get() = _word

    private val _loading = MutableLiveData(true)
    val loading : LiveData<Boolean>
        get() = _loading

    private val repository: MainRepositoryImpl
    val allColours: LiveData<List<Colour>>

    init {
        val coloursDao = ColourDatabase.getDatabase(application).colourDatabaseDao
        repository = MainRepositoryImpl(colourDao = coloursDao)
        allColours = repository.allColours
    }

    fun getRandomHexColour(): String? {
        return Random.nextInt(0, 16777215).toHexColourString()
    }

    fun getRandomWord() {
        _loading.postValue(true)
        _playSplatFx.value = false
        val job = viewModelScope.launch(Dispatchers.IO) {
            delay(2000)
            _word.postValue(repository.getRandomWord())
        }

        job.invokeOnCompletion {
            _loading.postValue(false)
            _playSplatFx.value = true
        }
    }

    fun insert(colour: Colour) = viewModelScope.launch(Dispatchers.IO) {
        repository.insertColour(colour)
    }

    fun deleteAll() = viewModelScope.launch(Dispatchers.IO) {
        repository.deleteColours()
    }
}
