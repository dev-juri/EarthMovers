package com.earthmovers.app.data.repository


import androidx.lifecycle.LiveData
import com.earthmovers.app.data.NetworkResult
import com.earthmovers.app.data.domain.DomainNotification
import com.earthmovers.app.data.domain.RecentProject
import com.earthmovers.app.data.domain.User
import com.earthmovers.app.data.local.entity.DbRecentPost
import com.earthmovers.app.data.local.entity.DbUser
import com.earthmovers.app.data.remote.*
import okhttp3.MultipartBody
import okhttp3.RequestBody


interface MainRepository {

    fun fetchUserDetailsIfAny(): LiveData<User?>

    suspend fun login(loginBody: LoginBody): NetworkResult<LoginResponseBody>

    suspend fun register(signupBody: RequestBody): NetworkResult<RegisterResponseBody>

    fun fetchRecentPostsFromDb(): LiveData<List<RecentProject>>

    suspend fun makePost(postBody: MultipartBody): NetworkResult<MakePostResponseBody>

    suspend fun createVendor(createVendorBody: MultipartBody): NetworkResult<CreateVendorResponse>

    suspend fun getUserWithId(getUserBody: GetUserBody): NetworkResult<NetworkUserModel>

    suspend fun getAllRemotePosts(): NetworkResult<PostsResponseBody>

    suspend fun savePosts(dbPosts: Array<DbRecentPost>)

    suspend fun getAllNotifications(notificationBody: NotificationBody): NetworkResult<NotificationResponse>

    fun fetchNotifications(): LiveData<List<DomainNotification>>

    fun getDbPostWithId(id: String): LiveData<RecentProject>

    suspend fun updateProfileDetails(updateProfileBody: MultipartBody): NetworkResult<UpdateResponse>

    suspend fun saveUserDetails(dbUser: DbUser)

    suspend fun acceptOffer(acceptOfferBody: AcceptOfferBody): NetworkResult<AcceptOfferResponse>

    suspend fun forgotPassword(forgotPasswordBody: ForgotPasswordBody): NetworkResult<ForgotPasswordResponse>

    suspend fun resetPassword(changePasswordBody: ChangePasswordBody): NetworkResult<ChangePasswordResponse>

}