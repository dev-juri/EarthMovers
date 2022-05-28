package com.earthmovers.www.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.earthmovers.www.data.repository.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PostsViewModel @Inject constructor(private val repository: MainRepository): ViewModel() {

    val posts = repository.fetchRecentPostsFromDb()
    val user = repository.fetchUserDetailsIfAny()

    init {
        fetchRemotePosts()
    }

    fun fetchRemotePosts() {
        viewModelScope.launch {
            repository.fetchRemoteRecentPosts()
        }
    }
}