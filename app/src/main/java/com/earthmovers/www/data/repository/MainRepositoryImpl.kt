package com.earthmovers.www.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.earthmovers.www.data.NetworkResult
import com.earthmovers.www.data.domain.DomainNotification
import com.earthmovers.www.data.domain.RecentProject
import com.earthmovers.www.data.domain.User
import com.earthmovers.www.data.local.dao.EarthMoversDao
import com.earthmovers.www.data.local.entity.DbRecentPost
import com.earthmovers.www.data.mapper.*
import com.earthmovers.www.data.remote.*
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

class MainRepositoryImpl @Inject constructor(
    private val localSource: EarthMoversDao,
    private val remoteSource: EarthMoversService,
    private val dispatcher: CoroutineDispatcher
) : MainRepository {

    override fun fetchUserDetailsIfAny(): LiveData<User?> =
        Transformations.map(localSource.getLoggedUserInfo()) {
            it.toDomainUSer()
        }

    override suspend fun login(loginBody: LoginBody): NetworkResult<LoginResponseBody> =
        withContext(dispatcher) {
            return@withContext try {
                val response = remoteSource.login(loginBody)
                if (response.isSuccessful && (response.body() as LoginResponseBody).message == "Login Succesful") {
                    val data = response.body() as LoginResponseBody
                    localSource.saveLoggedUserInfo(data.toDatabaseModel())

                    NetworkResult.Success(data)
                } else {
                    NetworkResult.Error("Incorrect username or password")
                }
            } catch (e: Exception) {
                NetworkResult.Error("Incorrect username or password")
            }
        }

    override suspend fun register(signupBody: RequestBody): NetworkResult<RegisterResponseBody> =
        withContext(dispatcher) {
            return@withContext try {
                val response = remoteSource.register(signupBody)
                if (response.isSuccessful && (response.body() as RegisterResponseBody).message == "Login Successful") {
                    val data = response.body() as RegisterResponseBody
                    localSource.saveLoggedUserInfo(data.toDatabaseModel())

                    NetworkResult.Success(data)
                } else {
                    NetworkResult.Error("User exists, proceed to login")
                }
            } catch (e: Exception) {
                NetworkResult.Error("User exists, proceed to login")
            }
        }

    override fun fetchRecentPostsFromDb(): LiveData<List<RecentProject>> =
        Transformations.map(localSource.fetchAllRecentPosts()) {
            it.toDomainPost()
        }

    override suspend fun makePost(postBody: MultipartBody): NetworkResult<MakePostResponseBody> =
        withContext(dispatcher) {
            return@withContext try {
                val response = remoteSource.makePost(postBody)
                if (response.isSuccessful) {
                    val data = (response.body() as MakePostResponseBody)

                    NetworkResult.Success(data)
                } else {
                    NetworkResult.Error("Something went wrong")
                }
            } catch (e: Exception) {
                NetworkResult.Error("Something went wrong, please try again.")
            }
        }

    override suspend fun createVendor(createVendorBody: MultipartBody): NetworkResult<CreateVendorResponse> =
        withContext(dispatcher) {
            return@withContext try {
                val response = remoteSource.createVendor(createVendorBody)
                if (response.isSuccessful) {
                    val data = (response.body() as CreateVendorResponse)

                    NetworkResult.Success(data)
                } else {
                    NetworkResult.Error("Something went wrong")
                }
            } catch (e: Exception) {
                NetworkResult.Error("Something went wrong, please try again.")
            }
        }

    override suspend fun getUserWithId(getUserBody: GetUserBody): NetworkResult<NetworkUserModel> {
        val response = remoteSource.getUserWithIdAsync(getUserBody)
        return try {
            val result = response.body() as NetworkUserModel
            NetworkResult.Success(result)
        } catch (e: Exception) {
            NetworkResult.Error("Something went wrong, please try again.")
        }
    }

    override suspend fun getAllRemotePosts(): NetworkResult<PostsResponseBody> =
        withContext(dispatcher) {
            val response = remoteSource.getAllRemotePostsAsync()
            return@withContext try {
                val result = response.body() as PostsResponseBody

                NetworkResult.Success(result)
            } catch (e: Exception) {
                NetworkResult.Error("Something went wrong, please try again.")
            }
        }

    override suspend fun savePosts(dbPosts: Array<DbRecentPost>) {
        localSource.deleteAllPosts()
        localSource.saveRecentPosts(*dbPosts)
    }

    override suspend fun getAllNotifications(notificationBody: NotificationBody): NetworkResult<NotificationResponse> =
        withContext(dispatcher) {
            val response = remoteSource.getAllNotificationsAsync(notificationBody)
            return@withContext try {
                val result = response.body() as NotificationResponse
                if (result.response.isNotEmpty()) {
                    localSource.saveNotifications(*result.toDbModel())
                }
                NetworkResult.Success(result)
            } catch (e: Exception) {
                NetworkResult.Error("Something went wrong, please try again.")
            }
        }

    override fun fetchNotifications(): LiveData<List<DomainNotification>> =
        Transformations.map(localSource.getAllNotifications()) {
            it.toDomainNotification()
        }

    override fun getDbPostWithId(id: String): LiveData<RecentProject> =
        Transformations.map(localSource.getPostDetails(id)) {
            it.toDomainModel()
        }

    override suspend fun updateProfileDetails(updateProfileBody: MultipartBody): NetworkResult<UpdateResponse> = withContext(dispatcher) {
        val response = remoteSource.updateProfile(updateProfileBody)
        return@withContext try {
            val result = response.body() as UpdateResponse

            NetworkResult.Success(result)
        } catch (e: Exception) {
            NetworkResult.Error("Something went wrong, please try again.")
        }
    }

}