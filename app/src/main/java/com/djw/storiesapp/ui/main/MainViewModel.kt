package com.djw.storiesapp.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.djw.storiesapp.data.repository.StoryRepository
import com.djw.storiesapp.data.repository.UserRepository
import kotlinx.coroutines.launch

class MainViewModel(private val userRepository: UserRepository, private val storyRepository: StoryRepository) : ViewModel(){
    fun getToken() : LiveData<String>{
        return userRepository.getToken().asLiveData()
    }

    fun isLogin() : LiveData<Boolean> {
        return userRepository.isLogin().asLiveData()
    }

    fun logout() {
        viewModelScope.launch {
            userRepository.logout()
        }
    }

    fun getStories(token: String) = storyRepository.getStories(token)
}