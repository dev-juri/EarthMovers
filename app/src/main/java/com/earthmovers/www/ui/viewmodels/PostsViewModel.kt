package com.earthmovers.www.ui.viewmodels

import android.content.Context
import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.earthmovers.www.data.NetworkResult
import com.earthmovers.www.data.State
import com.earthmovers.www.data.domain.User
import com.earthmovers.www.data.repository.MainRepository
import com.earthmovers.www.utils.RealPathUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import javax.inject.Inject

@HiltViewModel
class PostsViewModel @Inject constructor(private val repository: MainRepository) : ViewModel() {

    val posts = repository.fetchRecentPostsFromDb()
    val user = repository.fetchUserDetailsIfAny()

    private val _dataState = MutableLiveData<State?>()
    val dataState get() = _dataState

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage get() = _errorMessage

    private val _imageURI = MutableLiveData<Uri>()
    val imageURI get() = _imageURI

    fun fetchPosts() {
        viewModelScope.launch {
            repository.fetchPosts()
        }
    }

    fun makePost(
        context: Context,
        jobDetails: String,
        location: String,
        phoneNumber: String,
        userDetails: User
    ) {

        val imageUri = imageURI.value
        val imagePath = RealPathUtil.returnImagePath(
            context,
            imageUri!!,
            userDetails.id
        )!!

        val postBody =
            MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("name", userDetails.name)
                .addFormDataPart("src", userDetails.id)
                .addFormDataPart("phone", phoneNumber)
                .addFormDataPart("details", jobDetails)
                .addFormDataPart("location", location)
                .addFormDataPart("owner", userDetails.id)
                .addFormDataPart(
                    "file",
                    userDetails.id,
                    File(imagePath).asRequestBody("application/octet-stream".toMediaTypeOrNull())
                )
                .addFormDataPart("filename", userDetails.id)
                .build()


        viewModelScope.launch {
            _dataState.postValue(State.LOADING)

            when (val result = repository.makePost(postBody)) {
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

    fun setImageData(uri: Uri) {
        _imageURI.value = uri
    }

    fun resetState() {
        _dataState.value = null
    }

    private fun clearErrorMessage() {
        _errorMessage.value = null
    }
}