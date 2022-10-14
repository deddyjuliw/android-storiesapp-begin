package com.djw.storiesapp.di

import com.djw.storiesapp.data.repository.StoryRepository
import com.djw.storiesapp.retrofit.ApiConfig

object StoryInjection {
    fun provideRepository(): StoryRepository {
        val apiService = ApiConfig.getApiService()
        return StoryRepository.getInstance(apiService)
    }
}