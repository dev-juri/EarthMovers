package com.earthmovers.www.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.earthmovers.www.data.NetworkResult
import com.earthmovers.www.data.domain.DomainNotification
import com.earthmovers.www.data.domain.RecentProject
import com.earthmovers.www.data.domain.User
import com.earthmovers.www.data.local.dao.EarthMoversDao
import com.earthmovers.www.data.local.entity.DbRecentPost
import com.earthmovers.www.data.local.entity.DbUser
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

    override fun fetchUserDetailsIfAny(): LiveData<User?> = localSource.getLoggedUserInfo().map {
        it.toDomainUSer()
    }

    override suspend fun login(loginBody: LoginBody): NetworkResult<LoginResponseBody> =
        withContext(dispatcher) {
            return@withContext try {
                val response = remoteSource.login(loginBody)
                if (response.isSuccessful && (response.body() as LoginResponseBody).message == "Login Succesful") {
                    val data = response.body() as LoginResponseBody
                    saveUserDetails(data.toDatabaseModel())
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
                    saveUserDetails(data.toDatabaseModel())
                    NetworkResult.Success(data)
                } else {
                    NetworkResult.Error("User exists, proceed to login")
                }
            } catch (e: Exception) {
                NetworkResult.Error("User exists, proceed to login")
            }
        }

    override fun fetchRecentPostsFromDb(): LiveData<List<RecentProject>> =
        localSource.fetchAllRecentPosts().map {
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

    override suspend fun getUserWithId(getUserBody: GetUserBody): NetworkResult<NetworkUserModel> =
        withContext(dispatcher) {
            return@withContext try {
                val response = remoteSource.getUserWithIdAsync(getUserBody)
                if (response.isSuccessful) {
                    val result = response.body() as NetworkUserModel
                    NetworkResult.Success(result)
                } else {
                    NetworkResult.Error("Something went wrong, please try again.")
                }
            } catch (e: Exception) {
                NetworkResult.Error("Something went wrong, please try again.")
            }
        }

    override suspend fun getAllRemotePosts(): NetworkResult<PostsResponseBody> =
        withContext(dispatcher) {
            val response = remoteSource.getAllRemotePostsAsync()
            return@withContext try {
                if (response.isSuccessful) {
                    val result = response.body() as PostsResponseBody

                    NetworkResult.Success(result)
                } else {
                    NetworkResult.Error("Something went wrong, please try again.")
                }
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
                if (response.isSuccessful) {
                    val result = response.body() as NotificationResponse
                    if (result.response.isNotEmpty()) {
                        localSource.saveNotifications(*result.toDbModel())
                    }
                    NetworkResult.Success(result)
                } else {
                    NetworkResult.Error("Something went wrong, please try again.")
                }
            } catch (e: Exception) {
                NetworkResult.Error("Something went wrong, please try again.")
            }
        }

    override fun fetchNotifications(): LiveData<List<DomainNotification>> =
        localSource.getAllNotifications().map {
            it.toDomainNotification()
        }

    override fun getDbPostWithId(id: String): LiveData<RecentProject> =
        localSource.getPostDetails(id).map {
            it.toDomainModel()
        }

    override suspend fun updateProfileDetails(updateProfileBody: MultipartBody): NetworkResult<UpdateResponse> =
        withContext(dispatcher) {
            return@withContext try {
                val response = remoteSource.updateProfile(updateProfileBody)
                if (response.isSuccessful) {
                    val result = response.body() as UpdateResponse
                    NetworkResult.Success(result)
                } else {
                    NetworkResult.Error("Something went wrong, please try again.")
                }
            } catch (e: Exception) {
                NetworkResult.Error("Something went wrong, please try again.")
            }
        }

    override suspend fun saveUserDetails(dbUser: DbUser) {
        localSource.saveLoggedUserInfo(dbUser)
    }

    override suspend fun acceptOffer(acceptOfferBody: AcceptOfferBody): NetworkResult<AcceptOfferResponse> =
        withContext(dispatcher) {
            return@withContext try {
                val result = remoteSource.acceptOffer(acceptOfferBody)
                if (result.isSuccessful) {
                    NetworkResult.Success(result.body() as AcceptOfferResponse)
                } else {
                    NetworkResult.Error("Something went wrong, please try again.")
                }

            } catch (e: Exception) {
                NetworkResult.Error("Something went wrong, please try again.")
            }
        }

    override suspend fun forgotPassword(forgotPasswordBody: ForgotPasswordBody): NetworkResult<ForgotPasswordResponse> =
        withContext(dispatcher) {
            return@withContext try {
                val response = remoteSource.forgotPassword(forgotPasswordBody)
                if (response.isSuccessful) {
                    val data = response.body() as ForgotPasswordResponse
                    NetworkResult.Success(data)
                } else {
                    NetworkResult.Error("User with email not found")
                }
            } catch (e: Exception) {
                NetworkResult.Error("Something went wrong")
            }
        }

    override suspend fun resetPassword(changePasswordBody: ChangePasswordBody): NetworkResult<ChangePasswordResponse> =
        withContext(dispatcher) {
            return@withContext try {
                val response = remoteSource.resetPassword(changePasswordBody)
                if (response.isSuccessful && (response.body() as ChangePasswordResponse).message == "Password Reset successful") {
                    val data = response.body() as ChangePasswordResponse

                    NetworkResult.Success(data)
                } else {
                    NetworkResult.Error("Something went wrong")
                }
            } catch (e: Exception) {
                NetworkResult.Error("Something went wrong")
            }
        }

}