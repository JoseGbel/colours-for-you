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

class MyColoursViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: MainRepositoryImpl
    val allColours: LiveData<List<Colour>>

    init {
        val coloursDao = ColourDatabase.getDatabase(application).colourDatabaseDao
        repository = MainRepositoryImpl(colourDao = coloursDao)
        allColours = repository.allColours
    }

    fun deleteAll() = viewModelScope.launch(Dispatchers.IO) {
        repository.deleteColours()
    }
}

class MyColoursViewModelFactory(private val application: Application) : ViewModelProvider.Factory{

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return MyColoursViewModel(application) as T
    }
}