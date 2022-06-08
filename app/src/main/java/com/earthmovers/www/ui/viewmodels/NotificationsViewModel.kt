package com.earthmovers.www.ui.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.earthmovers.www.data.NetworkResult
import com.earthmovers.www.data.State
import com.earthmovers.www.data.remote.GetUserBody
import com.earthmovers.www.data.remote.NotificationBody
import com.earthmovers.www.data.remote.UserResponse
import com.earthmovers.www.data.repository.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotificationsViewModel @Inject constructor(private val repository: MainRepository) :
    ViewModel() {
    val user = repository.fetchUserDetailsIfAny()

    val notifications = repository.fetchNotifications()

    private val _dataState = MutableLiveData<State?>()
    val dataState get() = _dataState

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage get() = _errorMessage

    private val _onlineUserInfo = MutableLiveData<UserResponse>()
    val onlineUserInfo get() = _onlineUserInfo

    fun fetchNotifications(notificationBody: NotificationBody) {
        viewModelScope.launch {
            _dataState.postValue(State.LOADING)

            when (val result = repository.getAllNotifications(notificationBody)) {
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

    fun getUserWithId(getUserBody: GetUserBody) {
        viewModelScope.launch {
            when (val result = repository.getUserWithId(getUserBody)) {
                is NetworkResult.Success -> {
                    _onlineUserInfo.value = result.data.response
                }
                else -> {
                    resetState()
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