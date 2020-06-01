package com.remcode.coloursforyou.business

import android.app.Application
import androidx.lifecycle.*
import com.remcode.coloursforyou.data.local.ColourDatabase
import com.remcode.coloursforyou.data.models.Colour
import com.remcode.coloursforyou.data.repository.MainRepository
import com.remcode.coloursforyou.data.repository.MainRepositoryImpl
import com.remcode.coloursforyou.utils.DefaultDispatcherProvider
import com.remcode.coloursforyou.utils.DispatcherProvider
import kotlinx.coroutines.*

class MyColoursViewModel(
    application: Application,
    private val repository: MainRepository
        = MainRepositoryImpl(colourDao = ColourDatabase.getDatabase(application).colourDao),
    private val dispatchers: DispatcherProvider = DefaultDispatcherProvider()
) : AndroidViewModel(application) {

    val allColours: LiveData<List<Colour>> = repository.allColours

    fun deleteAll() = viewModelScope.launch(dispatchers.io()) {
        repository.deleteColours()
    }
}

class MyColoursViewModelFactory(private val application: Application) : ViewModelProvider.Factory{

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return MyColoursViewModel(application) as T
    }
}