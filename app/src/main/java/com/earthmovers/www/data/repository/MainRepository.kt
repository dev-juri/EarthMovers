package com.earthmovers.www.data.repository

import androidx.lifecycle.LiveData
import com.earthmovers.www.data.NetworkResult
import com.earthmovers.www.data.domain.User
import com.earthmovers.www.data.remote.LoginBody
import com.earthmovers.www.data.remote.LoginResponseBody
import com.earthmovers.www.data.remote.RegisterResponseBody
import okhttp3.RequestBody


interface MainRepository {

    suspend fun fetchUserDetailsIfAny(): LiveData<User?>

    suspend fun login(loginBody: LoginBody): NetworkResult<LoginResponseBody>

    suspend fun register(signupBody: RequestBody): NetworkResult<RegisterResponseBody>
}