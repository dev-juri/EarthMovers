package com.earthmovers.www.ui.onboarding.forgot_password

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.earthmovers.www.data.NetworkResult
import com.earthmovers.www.data.State
import com.earthmovers.www.data.remote.ChangePasswordBody
import com.earthmovers.www.data.remote.ForgotPasswordBody
import com.earthmovers.www.data.remote.LoginBody
import com.earthmovers.www.data.repository.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import javax.inject.Inject

@HiltViewModel
class ForgotPasswordViewModel @Inject constructor(private val repository: MainRepository): ViewModel() {

    private val _dataState = MutableLiveData<State?>()
    val dataState get() = _dataState

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage get() = _errorMessage

    private val _userID = MutableLiveData<String>()
    val userID get() = _userID

    fun forgotPassword(forgotPasswordBody: ForgotPasswordBody) {
        viewModelScope.launch {
            _dataState.postValue(State.LOADING)
            when (val result = repository.forgotPassword(forgotPasswordBody)) {
                is NetworkResult.Success -> {
                    clearErrorMessage()
                    _userID.value = result.data._id
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

    fun resetPassword(changePasswordBody: ChangePasswordBody) {
        viewModelScope.launch {
            _dataState.postValue(State.LOADING)

            when (val result = repository.resetPassword(changePasswordBody)) {
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