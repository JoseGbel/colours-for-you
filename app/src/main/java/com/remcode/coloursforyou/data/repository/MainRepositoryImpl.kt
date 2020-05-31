package com.remcode.coloursforyou.data.repository

import androidx.lifecycle.LiveData
import com.remcode.coloursforyou.data.local.ColourDao
import com.remcode.coloursforyou.data.models.Colour
import com.remcode.coloursforyou.network.ColourNamesService
import com.remcode.coloursforyou.network.ColourNamesServiceFactory

class MainRepositoryImpl(private val colourNamesService : ColourNamesService = ColourNamesServiceFactory.createService(),
                         private val colourDao : ColourDao)
    : MainRepository {

    override val allColours: LiveData<List<Colour>> = colourDao.getColours()

    override suspend fun getRandomWord(): List<String> {
        return colourNamesService.getRandomWord()
    }

    override suspend fun getWords(number: Int): List<String> {
        return colourNamesService.getWords(number)
    }

    override suspend fun insertColour(colour: Colour) {
        colourDao.insertColour(colour)
    }

    override suspend fun deleteColours() {
        colourDao.deleteAllColours()
    }
}