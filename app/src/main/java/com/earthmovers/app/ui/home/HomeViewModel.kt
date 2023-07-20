package com.earthmovers.app.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.earthmovers.app.data.NetworkResult
import com.earthmovers.app.data.State
import com.earthmovers.app.data.domain.RecentProject
import com.earthmovers.app.data.domain.User
import com.earthmovers.app.data.mapper.toDbModel
import com.earthmovers.app.data.remote.AcceptOfferBody
import com.earthmovers.app.data.remote.GetUserBody
import com.earthmovers.app.data.remote.UserResponse
import com.earthmovers.app.data.repository.MainRepository
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val repository: MainRepository) : ViewModel() {

    private val _projectLocation = MutableLiveData<LatLng>()
    val projectLocation get() = _projectLocation

    private val _selectedPost = MutableLiveData<RecentProject>()
    val selectedPost: LiveData<RecentProject> get() = _selectedPost

    val posts = repository.fetchRecentPostsFromDb()
    val user = repository.fetchUserDetailsIfAny()

    private val _dataState = MutableLiveData<State?>()
    val dataState get() = _dataState

    private val _onlineUserInfo = MutableLiveData<UserResponse>()
    val onlineUserInfo get() = _onlineUserInfo

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage get() = _errorMessage

    init {
        getRemotePosts()
    }

    fun acceptOffer(userDetails: User) {
        val acceptOfferBody = AcceptOfferBody(
            accepterID = userDetails.id,
            accepterImage = userDetails.src!!,
            accepterName = userDetails.name,
            postID = selectedPost.value!!._id
        )

        viewModelScope.launch {
            _dataState.postValue(State.LOADING)
            when (repository.acceptOffer(acceptOfferBody)) {
                is NetworkResult.Success -> {
                    _dataState.postValue(State.SUCCESS)
                    clearErrorMessage()
                }
                is NetworkResult.Error -> {
                    _dataState.postValue(State.ERROR)
                }
                else -> {
                    _dataState.postValue(State.LOADING)
                    clearErrorMessage()
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

    fun getRemotePosts() {
        viewModelScope.launch {
            _dataState.value = State.LOADING
            when (val result = repository.getAllRemotePosts()) {
                is NetworkResult.Success -> {
                    if (result.data.response.isNotEmpty()) {
                        repository.savePosts(result.data.toDbModel())
                    }
                    resetState()
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

    fun selectPost(selectedPost: RecentProject) {
        _selectedPost.postValue(selectedPost)
    }

    private fun clearErrorMessage() {
        _errorMessage.value = null
    }

    fun projectLocationReady(location: LatLng) {
        _projectLocation.value = location
    }
}
