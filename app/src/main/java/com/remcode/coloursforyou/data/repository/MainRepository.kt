package com.remcode.coloursforyou.data.repository

interface MainRepository {
    suspend fun getRandomWord() : List<String>
    suspend fun getWords(number: Int) : List<String>
}
