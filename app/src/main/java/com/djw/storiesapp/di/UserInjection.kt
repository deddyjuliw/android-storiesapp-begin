package com.djw.storiesapp.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.djw.storiesapp.data.repository.UserRepository
import com.djw.storiesapp.retrofit.ApiConfig


private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

object UserInjection {
    fun providePreferences(context: Context): UserRepository {
        val apiService = ApiConfig.getApiService()
        return UserRepository.getInstance(context.dataStore, apiService)
    }
}