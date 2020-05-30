package com.remcode.coloursforyou.business

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.remcode.coloursforyou.data.local.ColourDatabase
import com.remcode.coloursforyou.data.models.Colour
import com.remcode.coloursforyou.data.repository.MainRepository
import com.remcode.coloursforyou.data.repository.MainRepositoryImpl
import com.remcode.coloursforyou.utils.DefaultDispatcherProvider
import com.remcode.coloursforyou.utils.DispatcherProvider
import com.remcode.coloursforyou.utils.toHexColourString
import kotlinx.coroutines.*
import java.lang.Exception
import kotlin.random.Random

class ColourGeneratorViewModel(application: Application,
                               val repository: MainRepository = MainRepositoryImpl(colourDao = ColourDatabase.getDatabase(application).colourDatabaseDao),
                               private val dispatchers: DispatcherProvider = DefaultDispatcherProvider())
    : AndroidViewModel(application) {

    private val TAG = "AppDebug"

    private val _playSplatFx = MutableLiveData(false)
    val playSplatFx: LiveData<Boolean>
        get() = _playSplatFx

    private val _word = MutableLiveData<List<String>>()
    val word : LiveData<List<String>>
        get() = _word

    private val _loading = MutableLiveData(false)
    val loading : LiveData<Boolean>
        get() = _loading

//    private val repository: MainRepositoryImpl

    init {
//        val coloursDao = ColourDatabase.getDatabase(application).colourDatabaseDao
//        repository = MainRepositoryImpl(colourDao = coloursDao)
    }

    fun getRandomHexColour(): String? {
        return Random.nextInt(0, 16777215).toHexColourString()
    }

    fun getRandomWord(colour : String?) {
        _loading.postValue(true)
        _playSplatFx.value = false

        try {
            val handler = CoroutineExceptionHandler { _, exception ->
                Log.e(TAG, "CoroutineExceptionHandler got $exception")
            }
            val job = viewModelScope.launch(dispatchers.io() + handler) {
                _word.postValue(repository.getRandomWord())
                delay(2000)
            }

            job.invokeOnCompletion {
                if (colour != null)
                    insert(Colour(colour, word.value!![0]))
                _loading.postValue(false)
                _playSplatFx.postValue(true)
            }
        }catch (exception: Exception){
            // network call's unhappy path ... do something
            if (exception.message != null)
                Log.e(TAG, exception.message!!)
        }
    }

    fun insert(colour: Colour) = viewModelScope.launch(dispatchers.io()) {
        repository.insertColour(colour)
    }
}

class ColourGeneratorViewModelFactory(private val application: Application) : ViewModelProvider.Factory{

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return ColourGeneratorViewModel(application) as T
    }
}
