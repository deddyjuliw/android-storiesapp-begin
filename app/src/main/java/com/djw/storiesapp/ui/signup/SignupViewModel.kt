package com.djw.storiesapp.ui.signup

import androidx.lifecycle.ViewModel
import com.djw.storiesapp.data.repository.UserRepository

class SignupViewModel(private val userRepository: UserRepository) : ViewModel() {

    fun register(name: String, email: String, password: String) = userRepository.register(name, email, password)

}