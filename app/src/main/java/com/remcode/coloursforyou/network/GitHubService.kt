package com.remcode.coloursforyou.network

import retrofit2.http.GET
import retrofit2.http.Query

interface GitHubService {

    @GET("/word?number=1")
    suspend fun getRandomWord() : List<String>

    @GET("/word")
    suspend fun getWords(@Query("number") number: Int) : List<String>
}
