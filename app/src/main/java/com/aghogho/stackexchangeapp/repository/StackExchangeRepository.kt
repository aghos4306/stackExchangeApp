package com.aghogho.stackexchangeapp.repository

import com.aghogho.stackexchangeapp.service.StackExchangeAPI
import javax.inject.Inject

class StackExchangeRepository @Inject constructor(
    private val stackExchangeAPI: StackExchangeAPI
) {

    suspend fun fetchQuestions() = stackExchangeAPI.fetchQuestions()

    suspend fun searchQuestions(query: String) = stackExchangeAPI.searchQuestions(query = query)

    suspend fun searchWithFilterTags(tags: String) = stackExchangeAPI.searchWithFilterTags(tags = tags)

}