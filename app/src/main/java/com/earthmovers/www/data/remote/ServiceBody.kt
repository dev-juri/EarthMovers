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