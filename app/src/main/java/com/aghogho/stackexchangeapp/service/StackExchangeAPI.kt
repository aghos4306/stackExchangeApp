package com.aghogho.stackexchangeapp.service

import com.aghogho.stackexchangeapp.model.StackExchangeResponse
import com.aghogho.stackexchangeapp.utils.Constant.Companion.API_KEY
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface StackExchangeAPI {

    @GET("2.2/questions")
    suspend fun fetchQuestions(
        @Query("key")
        key: String = API_KEY,
        @Query("order")
        order: String = "desc",
        @Query("sort")
        sort: String = "activity",
        @Query("site")
        site: String = "stackoverflow",
        @Query("page")
        page: Int = 1,
        @Query("pageSize")
        pageSize: Int = 20
    ): Response<StackExchangeResponse>

    @GET("2.3/search")
    suspend fun searchQuestions(
        @Query("key")
        key: String = API_KEY,
        @Query("intitle")
        query: String,
        @Query("order")
        order: String = "desc",
        @Query("sort")
        sort: String = "votes",
        @Query("site")
        site: String = "stackoverflow",
        @Query("page")
        page: Int = 1,
        @Query("pageSize")
        pageSize: Int = 25
    ): Response<StackExchangeResponse>

    @GET("/2.3/search")
    suspend fun searchWithFilterTags(
        @Query("key")
        key: String = API_KEY,
        @Query("tagged")
        tags: String,
        @Query("order")
        order: String = "desc",
        @Query("sort")
        sort: String = "votes",
        @Query("site")
        site: String = "stackoverflow",
        @Query("page")
        page: Int = 1,
        @Query("pageSize")
        pageSize: Int = 25
    ): Response<StackExchangeResponse>

}