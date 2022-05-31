package com.earthmovers.www.ui.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.earthmovers.www.data.NetworkResult
import com.earthmovers.www.data.State
import com.earthmovers.www.data.repository.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val repository: MainRepository) : ViewModel() {


    val posts = repository.fetchRecentPostsFromDb()
    val user = repository.fetchUserDetailsIfAny()

    private val _dataState = MutableLiveData<State?>()
    val dataState get() = _dataState

    init {
        getRemotePosts()
    }
    fun getRemotePosts() {
        Timber.tag("POST").d("getRemotePost called")

        _dataState.value = State.LOADING

        viewModelScope.launch {
            Timber.tag("POST").d("Loading...")
            when (repository.getAllRemotePosts()) {
                is NetworkResult.Success -> {
                    _dataState.postValue(State.SUCCESS)
                    Timber.tag("POST").d("Successful")
                }
                is NetworkResult.Error -> {
                    _dataState.postValue(State.ERROR)
                    Timber.tag("POST").d("Error")
                }
                else -> {
                    _dataState.postValue(State.LOADING)
                }
            }
        }
    }

    fun resetState() {
        _dataState.value = null
    }

}
