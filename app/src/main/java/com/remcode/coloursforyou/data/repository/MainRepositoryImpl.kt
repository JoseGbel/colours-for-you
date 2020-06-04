package com.remcode.coloursforyou.data.repository

import androidx.lifecycle.LiveData
import com.remcode.coloursforyou.data.local.ColourDao
import com.remcode.coloursforyou.data.models.Colour
import com.remcode.coloursforyou.data.network.ColourNamesService
import com.remcode.coloursforyou.data.network.ColourNamesServiceFactory
import okhttp3.MediaType
import okhttp3.ResponseBody
import retrofit2.HttpException
import retrofit2.Response
import retrofit2.Retrofit
import java.lang.Exception
import java.lang.RuntimeException
import java.net.SocketTimeoutException

class MainRepositoryImpl(private val colourNamesService : ColourNamesService = ColourNamesServiceFactory.createService(),
                         private val colourDao : ColourDao)
    : MainRepository {

    override val allColours: LiveData<List<Colour>> = colourDao.getColours()

    override suspend fun getRandomWord(): List<String> {
        return colourNamesService.getRandomWord()
    }

    override suspend fun insertColour(colour: Colour) {
        colourDao.insertColour(colour)
    }

    override suspend fun deleteColours() {
        colourDao.deleteAllColours()
    }
}