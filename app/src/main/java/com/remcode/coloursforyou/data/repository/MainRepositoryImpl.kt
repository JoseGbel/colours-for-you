package com.remcode.coloursforyou.data.repository

import androidx.lifecycle.LiveData
import com.remcode.coloursforyou.data.local.ColourDatabaseDao
import com.remcode.coloursforyou.data.models.Colour
import com.remcode.coloursforyou.network.GitHubService
import com.remcode.coloursforyou.network.GitHubServiceFactory

class MainRepositoryImpl(private val gitHubService : GitHubService = GitHubServiceFactory.createService(),
                         private val colourDao : ColourDatabaseDao)
    : MainRepository {

    override val allColours: LiveData<List<Colour>> = colourDao.getColours()

    override suspend fun getRandomWord(): List<String> {
        return gitHubService.getRandomWord()
    }

    override suspend fun getWords(number: Int): List<String> {
        return gitHubService.getWords(number)
    }

    override suspend fun insertColour(colour: Colour) {
        colourDao.insertColour(colour)
    }

    override suspend fun deleteColours() {
        colourDao.deleteAllColours()
    }
}