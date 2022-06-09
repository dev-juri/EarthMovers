package com.earthmovers.www.data.remote

data class LoginBody(
    val userName: String,
    val password: String
)

data class GetUserBody(
    val userID: String
)

data class NotificationBody(
    val userID: String
)

data class AcceptOfferBody(
    val accepterID: String,
    val accepterName: String,
    val accepterImage: String,
    val postID: String
)

data class ForgotPasswordBody(
    val email: String
)

data class ChangePasswordBody(
    val userID: String,
    val password: String
)