package com.remcode.coloursforyou.data.repository

import androidx.lifecycle.LiveData
import com.remcode.coloursforyou.data.models.Colour

interface MainRepository {
    val allColours : LiveData<List<Colour>>

    suspend fun getRandomWord() : List<String>
    suspend fun getWords(number: Int) : List<String>

    suspend fun insertColour(colour: Colour)
    suspend fun deleteColours()
}
