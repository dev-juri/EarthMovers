package com.earthmovers.www.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.earthmovers.www.data.NetworkResult
import com.earthmovers.www.data.domain.RecentProject
import com.earthmovers.www.data.domain.User
import com.earthmovers.www.data.local.dao.EarthMoversDao
import com.earthmovers.www.data.mapper.toDatabaseModel
import com.earthmovers.www.data.mapper.toDbModel
import com.earthmovers.www.data.mapper.toDomainPost
import com.earthmovers.www.data.mapper.toDomainUSer
import com.earthmovers.www.data.remote.*
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
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
            try {
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
            try {
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

    override suspend fun fetchRemoteRecentPosts(): NetworkResult<PostsResponseBody> =
        withContext(dispatcher) {
            try {
                val response = remoteSource.fetchRecentPosts()
                if (response.isSuccessful) {
                    val data = (response.body() as PostsResponseBody)

                    if (data.response.isNotEmpty()) {
                        localSource.saveRecentPosts(*data.toDbModel())
                    }
                    NetworkResult.Success(data)
                } else {
                    NetworkResult.Error("Something went wrong")
                }
            } catch (e: Exception) {
                NetworkResult.Error("Something went wrong, please try again.")
            }
        }

    override fun fetchRecentPostsFromDb(): LiveData<List<RecentProject>> =
        Transformations.map(localSource.fetchAllRecentPosts()) {
            it.toDomainPost()
        }
}