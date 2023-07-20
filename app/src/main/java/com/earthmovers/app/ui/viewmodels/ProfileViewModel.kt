package com.earthmovers.app.ui.viewmodels

import android.content.Context
import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.earthmovers.app.data.NetworkResult
import com.earthmovers.app.data.State
import com.earthmovers.app.data.domain.User
import com.earthmovers.app.data.local.entity.DbUser
import com.earthmovers.app.data.remote.GetUserBody
import com.earthmovers.app.data.repository.MainRepository
import com.earthmovers.app.utils.RealPathUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(private val repository: MainRepository) : ViewModel() {

    val user = repository.fetchUserDetailsIfAny()
    private val _imageURI = MutableLiveData<Uri?>()
    val imageURI get() = _imageURI

    private val _profileLoaded = MutableLiveData<Boolean?>()
    val profileLoaded get() = _profileLoaded

    private val _dataState = MutableLiveData<State?>()
    val dataState get() = _dataState

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage get() = _errorMessage

    fun getUserWithId(user: User) {
        viewModelScope.launch {
            _dataState.value = State.LOADING
            when (val result = repository.getUserWithId(GetUserBody(user.id))) {
                is NetworkResult.Success -> {
                    val dbUser = DbUser(
                        token = user.token,
                        id = user.id,
                        phone = result.data.response.phone,
                        email = user.email,
                        name = result.data.response.name,
                        src = result.data.response.src,
                        description = result.data.response.description,
                        isVendor = result.data.response.isVendor ?: false,
                        truck_plate_number = result.data.response.truck_plate_number,
                        truck_src = result.data.response.truck_src
                    )
                    repository.saveUserDetails(dbUser)
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

    fun createVendor(
        context: Context, desc: String, phone: String, truckNum: String,
        userDetails: User
    ) {
        val imageUri = imageURI.value
        val imagePath = RealPathUtil.returnImagePath(
            context,
            imageUri!!,
            truckNum
        )!!

        val createVendorBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("userID", userDetails.id)
            .addFormDataPart("description", desc)
            .addFormDataPart("truck_plate_number", truckNum)
            .addFormDataPart("phone", phone)
            .addFormDataPart(
                "file", userDetails.id,
                File(imagePath).asRequestBody("application/octet-stream".toMediaTypeOrNull())
            )
            .addFormDataPart("filename", userDetails.id)
            .build()

        viewModelScope.launch {
            _dataState.postValue(State.LOADING)

            when (val result = repository.createVendor(createVendorBody)) {
                is NetworkResult.Success -> {
                    clearErrorMessage()
                    clearImageData()
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

    fun updateUser(context: Context, user: User) {
        val imageUri = imageURI.value
        val imagePath = RealPathUtil.returnImagePath(
            context,
            imageUri!!,
            user.id
        )!!
        val updateUserBody = MultipartBody.Builder().setType(MultipartBody.FORM)
            .addFormDataPart("name", user.name)
            .addFormDataPart("email", user.email)
            .addFormDataPart("phone", user.phone)
            .addFormDataPart("src", "")
            .addFormDataPart(
                "file", user.id,
                File(imagePath).asRequestBody("application/octet-stream".toMediaTypeOrNull())
            )
            .addFormDataPart("filename", user.id)
            .addFormDataPart("userID", user.id)
            .build()

        viewModelScope.launch {
            when (val result = repository.updateProfileDetails(updateUserBody)) {
                is NetworkResult.Success -> {
                    clearErrorMessage()
                    clearImageData()
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

    fun clearImageData() {
        _imageURI.value = null
    }

    private fun clearErrorMessage() {
        _errorMessage.value = null
    }

    fun profileLoaded(){
        _profileLoaded.value = true
    }
    fun clearProfileLoadedStatus() {
        _profileLoaded.postValue(null)
    }
}