package com.earthmovers.www.data.repository

import androidx.lifecycle.LiveData
import com.earthmovers.www.data.NetworkResult
import com.earthmovers.www.data.domain.RecentProject
import com.earthmovers.www.data.domain.User
import com.earthmovers.www.data.remote.*
import okhttp3.MultipartBody
import okhttp3.RequestBody


interface MainRepository {

    fun fetchUserDetailsIfAny(): LiveData<User?>

    suspend fun login(loginBody: LoginBody): NetworkResult<LoginResponseBody>

    suspend fun register(signupBody: RequestBody): NetworkResult<RegisterResponseBody>

    suspend fun fetchPosts(): NetworkResult<PostsResponseBody>

    fun fetchRecentPostsFromDb(): LiveData<List<RecentProject>>

    suspend fun makePost(postBody: MultipartBody): NetworkResult<MakePostResponseBody>

    suspend fun createVendor(createVendorBody: MultipartBody): NetworkResult<CreateVendorResponse>

    suspend fun getUserWithId(getUserBody: GetUserBody): NetworkResult<NetworkUserModel>
}