package com.earthmovers.www.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.earthmovers.www.data.NetworkResult
import com.earthmovers.www.data.State
import com.earthmovers.www.data.domain.RecentProject
import com.earthmovers.www.data.mapper.toDbModel
import com.earthmovers.www.data.repository.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val repository: MainRepository) : ViewModel() {

    private val _selectedPost = MutableLiveData<RecentProject>()
    val selectedPost: LiveData<RecentProject> get() = _selectedPost

    val posts = repository.fetchRecentPostsFromDb()
    val user = repository.fetchUserDetailsIfAny()

    private val _dataState = MutableLiveData<State?>()
    val dataState get() = _dataState

    init {
        getRemotePosts()
    }

    fun getRemotePosts() {
        viewModelScope.launch {
            _dataState.value = State.LOADING
            when (val result = repository.getAllRemotePosts()) {
                is NetworkResult.Success -> {
                    if (result.data.response.isNotEmpty()) {
                        repository.savePosts(result.data.toDbModel())
                    }
                    _dataState.postValue(State.SUCCESS)
                }
                is NetworkResult.Error -> {
                    _dataState.postValue(State.ERROR)
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

    fun selectPost(selectedPost: RecentProject) {
        _selectedPost.postValue(selectedPost)
    }

}
