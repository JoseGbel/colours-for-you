package com.remcode.coloursforyou.business

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.remcode.coloursforyou.data.local.ColourDatabase
import com.remcode.coloursforyou.data.models.Colour
import com.remcode.coloursforyou.data.repository.MainRepositoryImpl
import com.remcode.coloursforyou.utils.toHexColourString
import kotlinx.coroutines.*
import java.lang.Exception
import kotlin.random.Random

class ColourGeneratorViewModel(application: Application) : AndroidViewModel(application) {

    private val TAG = "AppDebug"

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
        try {
            val handler = CoroutineExceptionHandler { _, exception ->
                Log.e(TAG, "CoroutineExceptionHandler got $exception")
            }
            val job = viewModelScope.launch(Dispatchers.IO + handler) {
                delay(2000)
                _word.postValue(repository.getRandomWord())
            }

            job.invokeOnCompletion {
                _loading.postValue(false)
                _playSplatFx.postValue(true)
            }
        }catch (exception: Exception){
            // network call's unhappy path ...
            if (exception.message != null)
                Log.e(TAG, exception.message!!)
        }
    }

    fun insert(colour: Colour) = viewModelScope.launch(Dispatchers.IO) {
        repository.insertColour(colour)
    }

    fun deleteAll() = viewModelScope.launch(Dispatchers.IO) {
        repository.deleteColours()
    }
}
