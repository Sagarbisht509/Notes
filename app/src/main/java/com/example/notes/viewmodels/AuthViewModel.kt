package com.example.notes.viewmodels

import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.notes.models.UserRequest
import com.example.notes.models.UserResponse
import com.example.notes.repository.UserRepository
import com.example.notes.utils.NetworkResult
import dagger.hilt.android.HiltAndroidApp
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(private val userRepository: UserRepository) : ViewModel() {

    val userResponseLiveData: LiveData<NetworkResult<UserResponse>>
        get() = userRepository.userResponseLiveData

    fun loginUser(userRequest: UserRequest) {
        viewModelScope.launch {
            userRepository.loginUser(userRequest)
        }
    }

    fun registerUser(userRequest: UserRequest) {
        viewModelScope.launch {
            userRepository.registerUser(userRequest)
        }
    }

    fun validateCredentials(
        email: String,
        password: String,
        username: String,
        fromRegister: Boolean
    ): Pair<Boolean, String> {
        var result = Pair(true, "");

        if (email.isEmpty() || password.isEmpty() || (fromRegister && username.isEmpty())) {
            result = Pair(false, "Field cannot be empty");
        } else if (password.length < 6) {
            result = Pair(false, "Password length should be greater than 5");
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            result = Pair(false, "Please enter valid email");
        }

        return result
    }
}