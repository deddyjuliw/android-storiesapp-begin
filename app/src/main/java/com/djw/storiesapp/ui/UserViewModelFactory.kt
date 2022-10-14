package com.djw.storiesapp.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.djw.storiesapp.data.repository.UserRepository
import com.djw.storiesapp.di.UserInjection
import com.djw.storiesapp.ui.signin.SigninViewModel
import com.djw.storiesapp.ui.signup.SignupViewModel

class UserViewModelFactory(private val userRepository: UserRepository) : ViewModelProvider.NewInstanceFactory(){
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(SignupViewModel::class.java) -> {
                SignupViewModel(userRepository) as T
            }
            modelClass.isAssignableFrom(SigninViewModel::class.java) -> {
                SigninViewModel(userRepository) as T
            }
            else -> {
                throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
            }
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: UserViewModelFactory? = null

        @JvmStatic
        fun getInstance(context: Context): UserViewModelFactory {
            if (INSTANCE == null) {
                synchronized(UserViewModelFactory::class.java) {
                    INSTANCE = UserViewModelFactory(UserInjection.providePreferences(context))
                }
            }
            return INSTANCE as UserViewModelFactory
        }
    }
}