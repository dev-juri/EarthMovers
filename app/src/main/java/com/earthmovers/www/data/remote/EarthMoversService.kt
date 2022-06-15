package com.earthmovers.www.data.remote

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Url

interface EarthMoversService {

    @POST("/register")
    suspend fun register(@Body body: RequestBody): Response<RegisterResponseBody>

    @POST("/login")
    suspend fun login(@Body loginBody: LoginBody): Response<LoginResponseBody>

    @POST("/post")
    suspend fun makePost(@Body postBody: MultipartBody): Response<MakePostResponseBody>

    @POST("/user")
    suspend fun getUserWithIdAsync(@Body getUserBody: GetUserBody): Response<NetworkUserModel>

    @POST("/create")
    suspend fun createVendor(@Body vendorBody: MultipartBody): Response<CreateVendorResponse>

    @GET("/posts")
    suspend fun getAllRemotePostsAsync(): Response<PostsResponseBody>

    @POST("/get-notifications")
    suspend fun getAllNotificationsAsync(@Body notificationBody: NotificationBody): Response<NotificationResponse>

    @POST("/accept-offer")
    suspend fun acceptOffer(@Body acceptOfferBody: AcceptOfferBody): Response<AcceptOfferResponse>

    @POST("/update")
    suspend fun updateProfile(@Body updatedProfileBody: MultipartBody): Response<UpdateResponse>

    @POST("forget-password")
    suspend fun forgotPassword(@Body forgotPasswordBody: ForgotPasswordBody): Response<ForgotPasswordResponse>

    @POST("change-password")
    suspend fun resetPassword(@Body changePasswordBody: ChangePasswordBody): Response<ChangePasswordResponse>

    @GET
    suspend fun getDirection(@Url url: String): Response<DirectionResponseModel>
}