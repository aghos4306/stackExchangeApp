package com.aghogho.stackexchangeapp.di

import com.aghogho.stackexchangeapp.service.ApiClient
import com.aghogho.stackexchangeapp.service.StackExchangeAPI
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.create
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object Module {

    @Provides
    @Singleton
    fun providesApiClient(): ApiClient = ApiClient()

    @Provides
    @Singleton
    fun provideStackExchangeApi(apiClient: ApiClient): StackExchangeAPI =
        apiClient.retrofit.create(StackExchangeAPI::class.java)

}