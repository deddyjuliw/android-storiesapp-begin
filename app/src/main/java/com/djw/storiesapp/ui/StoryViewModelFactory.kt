package com.djw.storiesapp.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.djw.storiesapp.data.repository.StoryRepository
import com.djw.storiesapp.data.repository.UserRepository
import com.djw.storiesapp.di.StoryInjection
import com.djw.storiesapp.di.UserInjection
import com.djw.storiesapp.ui.main.MainViewModel
import com.djw.storiesapp.ui.story.AddStoryViewModel

class StoryViewModelFactory private constructor(private val userRepository: UserRepository, private val storyRepository: StoryRepository) : ViewModelProvider.NewInstanceFactory(){
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(MainViewModel::class.java) -> {
                MainViewModel(userRepository, storyRepository) as T
            }
            modelClass.isAssignableFrom(AddStoryViewModel::class.java) -> {
                AddStoryViewModel(userRepository, storyRepository) as T
            }
            else -> {
                throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
            }
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: StoryViewModelFactory? = null

        @JvmStatic
        fun getInstance(context: Context): StoryViewModelFactory {
            if (INSTANCE == null) {
                synchronized(StoryViewModelFactory::class.java) {
                    INSTANCE = StoryViewModelFactory(UserInjection.providePreferences(context), StoryInjection.provideRepository())
                }
            }
            return INSTANCE as StoryViewModelFactory
        }
    }
}