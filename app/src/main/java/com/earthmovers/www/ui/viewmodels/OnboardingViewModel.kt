package com.earthmovers.www.ui.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.earthmovers.www.data.NetworkResult
import com.earthmovers.www.data.State
import com.earthmovers.www.data.remote.LoginBody
import com.earthmovers.www.data.repository.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import javax.inject.Inject

@HiltViewModel
class OnboardingViewModel @Inject constructor(private val repository: MainRepository) :
    ViewModel() {

    val user = repository.fetchUserDetailsIfAny()

    private val _dataState = MutableLiveData<State?>()
    val dataState get() = _dataState

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage get() = _errorMessage

    fun loginUser(loginBody: LoginBody) {
        viewModelScope.launch {
            _dataState.postValue(State.LOADING)
            when (val result = repository.login(loginBody)) {
                is NetworkResult.Success -> {
                    clearErrorMessage()
                    _dataState.postValue(State.SUCCESS)
                }
                is NetworkResult.Error -> {
                    _dataState.postValue(State.ERROR)
                    _errorMessage.value = result.exception
                }
                else -> {
                    clearErrorMessage()
                    _dataState.postValue(State.LOADING)
                }
            }
        }
    }

    fun signupUser(name: String, email: String, phone: String, password: String) {
        viewModelScope.launch {
            _dataState.postValue(State.LOADING)

            val requestBody = MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("name", name)
                .addFormDataPart("email", email)
                .addFormDataPart("phone", phone)
                .addFormDataPart("password", password)
                .build()

            when (val result = repository.register(requestBody)) {
                is NetworkResult.Success -> {
                    clearErrorMessage()
                    _dataState.postValue(State.SUCCESS)
                }
                is NetworkResult.Error -> {
                    _dataState.postValue(State.ERROR)
                    _errorMessage.value = result.exception
                }
                else -> {
                    clearErrorMessage()
                    _dataState.postValue(State.LOADING)
                }
            }
        }
    }

    fun resetState() {
        _dataState.value = null
    }

    private fun clearErrorMessage() {
        _errorMessage.value = null
    }
}