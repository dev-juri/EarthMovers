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
class ProfileViewModel @Inject constructor(private val repository: MainRepository) : ViewModel() {

    val user = repository.fetchUserDetailsIfAny()
    private val _imageURI = MutableLiveData<Uri?>()
    private val imageURI get() = _imageURI

    private val _dataState = MutableLiveData<State?>()
    val dataState get() = _dataState

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage get() = _errorMessage

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

    fun setImageData(uri: Uri) {
        _imageURI.value = uri
    }

    fun resetState() {
        _dataState.value = null
    }

    private fun clearImageData() {
        _imageURI.value = null
    }

    private fun clearErrorMessage() {
        _errorMessage.value = null
    }
}