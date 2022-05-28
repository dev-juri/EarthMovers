package com.earthmovers.www.data.remote

import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface EarthMoversService {

    @POST("/register")
    suspend fun register(@Body body: RequestBody): Response<RegisterResponseBody>

    @POST("/login")
    suspend fun login(@Body loginBody: LoginBody): Response<LoginResponseBody>

    @GET("/posts")
    suspend fun fetchRecentPosts(): Response<PostsResponseBody>
}