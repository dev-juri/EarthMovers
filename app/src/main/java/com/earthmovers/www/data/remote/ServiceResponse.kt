package com.earthmovers.www.data.remote

data class ErrorResponse(
    val message: String
)

data class RegisterResponseBody(
    val message: String,
    val token: String,
    val id: String,
    val phone: String,
    val email: String,
    val user: NetworkUserModel
)

data class NetworkUserModel(
    val _id: String,
    val name: String,
    val email: String,
    val phone: String,
    val password: String,
    val createdAt: String,
    val updatedAt: String,
    val __v: Int
)

data class LoginResponseBody(
    val message: String,
    val token: String,
    val id: String,
    val response: NetworkUserModel
)

data class PostsResponseBody(
    val response: ArrayList<PostDetails>
)

data class PostDetails(
    val _id: String,
    val name: String,
    val src: String,
    val phone: String,
    val details: String,
    val location: String,
    val owner: String,
    val image: String,
    val createdAt: String,
    val updatedAt: String,
    val __v: Int
)