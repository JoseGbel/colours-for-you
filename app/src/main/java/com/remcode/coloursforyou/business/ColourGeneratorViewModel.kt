package com.remcode.coloursforyou.business

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.remcode.coloursforyou.data.local.ColourDatabase
import com.remcode.coloursforyou.data.models.Colour
import com.remcode.coloursforyou.data.repository.MainRepository
import com.remcode.coloursforyou.data.repository.MainRepositoryImpl
import com.remcode.coloursforyou.presentation.FailedConnectionDialogFragment
import com.remcode.coloursforyou.utils.*
import kotlinx.coroutines.*
import retrofit2.HttpException
import java.lang.Exception
import java.net.SocketTimeoutException
import kotlin.random.Random

class ColourGeneratorViewModel(
    application: Application,
    private val repository: MainRepository = MainRepositoryImpl(
        colourDao = ColourDatabase.getDatabase(
            application
        ).colourDao
    ),
    private val dispatchers: DispatcherProvider = DefaultDispatcherProvider()
) : AndroidViewModel(application) {

    private val TAG = "AppDebug"

    val command = SingleLiveEvent<Command>()

    private val _colourLiveData = MutableLiveData<Colour>()
    val colourLiveData: LiveData<Colour>
        get() = _colourLiveData

    private val _loadingLiveData = MutableLiveData(false)
    val loadingLiveData: LiveData<Boolean>
        get() = _loadingLiveData

    fun generateRandomHexColour(): String {
        return Random.nextInt(0, 16777215).toHexColourString()
    }

    fun fetchNewColour(colour: String) {
        _loadingLiveData.postValue(true)

        val handler = CoroutineExceptionHandler { _, exception ->
            Log.e(TAG, "CoroutineExceptionHandler got $exception")
        }
        EspressoIdlingResource.increment()
        val job = viewModelScope.launch(dispatchers.io() + handler) {
            try {
                val word = repository.getRandomWord()
                _colourLiveData.postValue(Colour(colour, word[0]))
                delay(2000)
            } catch (exception: Exception) {
                // network call's unhappy path ... do something
                if (exception.message != null)
                    Log.e(TAG, exception.message!!)
            }
        }
        job.invokeOnCompletion {
            EspressoIdlingResource.decrement()
            _loadingLiveData.postValue(false)
            command.postValue(Command.PlaySoundEffect())
        }
    }

    fun insert(colour: Colour) = viewModelScope.launch(dispatchers.io()) {
        repository.insertColour(colour)
    }

    sealed class Command {
        class PlaySoundEffect : Command()
    }
}

class ColourGeneratorViewModelFactory(private val application: Application) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return ColourGeneratorViewModel(application) as T
    }
}

