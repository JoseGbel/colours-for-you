package com.remcode.coloursforyou.data.repository

import com.remcode.coloursforyou.network.GitHubService
import com.remcode.coloursforyou.network.GitHubServiceFactory

class MainRepositoryImpl(private val gitHubService : GitHubService = GitHubServiceFactory.createService())
    : MainRepository {

    override suspend fun getRandomWord(): List<String> {
        return gitHubService.getRandomWord()
    }

    override suspend fun getWords(number: Int): List<String> {
        return gitHubService.getWords(number)
    }
}