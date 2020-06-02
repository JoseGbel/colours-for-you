package com.remcode.coloursforyou.business

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.remcode.coloursforyou.data.local.ColourDatabase
import com.remcode.coloursforyou.data.models.Colour
import com.remcode.coloursforyou.data.repository.MainRepository
import com.remcode.coloursforyou.data.repository.MainRepositoryImpl
import com.remcode.coloursforyou.utils.*
import kotlinx.coroutines.*
import java.lang.Exception
import kotlin.random.Random

class ColourGeneratorViewModel(application: Application,
                               private val repository: MainRepository = MainRepositoryImpl(colourDao = ColourDatabase.getDatabase(application).colourDao),
                               private val dispatchers: DispatcherProvider = DefaultDispatcherProvider()
)
    : AndroidViewModel(application) {

    private val TAG = "AppDebug"

    val command = SingleLiveEvent<Command>()

    private val _wordLiveData = MutableLiveData<List<String>>()
    val wordLiveData : LiveData<List<String>>
        get() = _wordLiveData

    private val _colourLiveData = MutableLiveData<Colour>()
    val colourLiveData : LiveData<Colour>
        get() = _colourLiveData

    private val _loadingLiveData = MutableLiveData(false)
    val loadingLiveData : LiveData<Boolean>
        get() = _loadingLiveData

    fun generateRandomHexColour(): String {
        return Random.nextInt(0, 16777215).toHexColourString()
    }

    fun fetchNewColour(colour : String) {
        _loadingLiveData.postValue(true)

        try {
            val handler = CoroutineExceptionHandler { _, exception ->
                Log.e(TAG, "CoroutineExceptionHandler got $exception")
            }
            EspressoIdlingResource.increment()
            val job = viewModelScope.launch(dispatchers.io() + handler) {
                val word = repository.getRandomWord()
                _wordLiveData.postValue(word)
                _colourLiveData.postValue(Colour(colour, word[0]))
                delay(2000)
            }

            job.invokeOnCompletion {
                EspressoIdlingResource.decrement()
                _loadingLiveData.postValue(false)
                command.postValue(Command.PlaySoundEffect())
            }
        } catch (exception: Exception){
            // network call's unhappy path ... do something
            if (exception.message != null)
                Log.e(TAG, exception.message!!)
        }
    }

    fun insert(colour: Colour) = viewModelScope.launch(dispatchers.io()) {
        repository.insertColour(colour)
    }

    sealed class Command {
        class PlaySoundEffect : Command()
    }
}

class ColourGeneratorViewModelFactory(private val application: Application) : ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return ColourGeneratorViewModel(application) as T
    }
}

