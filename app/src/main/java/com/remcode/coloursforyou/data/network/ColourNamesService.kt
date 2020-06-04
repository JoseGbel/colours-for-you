package com.remcode.coloursforyou.data.network

import retrofit2.http.GET

interface ColourNamesService {

    @GET("/word?number=1")
    suspend fun getRandomWord() : List<String>
}
