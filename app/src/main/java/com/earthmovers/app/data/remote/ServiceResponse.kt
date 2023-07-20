package com.earthmovers.app.data.remote

data class RegisterResponseBody(
    val message: String,
    val token: String,
    val id: String,
    val phone: String,
    val email: String,
    val user: UserNorm
)

data class UserNorm(
    val _id: String,
    val name: String,
    val email: String,
    val phone: String,
    val password: String,
    val createdAt: String,
    val updatedAt: String,
    val __v: Int,
    val src: String? = null,
    val description: String? = null,
    val isVendor: Boolean? = false,
    val truck_plate_number: String? = null,
    val truck_src: String? = null
)

data class NetworkUserModel(
    val response: UserResponse
)

data class UserResponse(
    val notifications: List<Notifications>,
    val _id: String,
    val name: String,
    val email: String,
    val phone: String,
    val password: String,
    val createdAt: String,
    val updatedAt: String,
    val __v: Int,
    val src: String? = null,
    val description: String? = null,
    val isVendor: Boolean? = false,
    val truck_plate_number: String? = null,
    val truck_src: String? = null
)

data class LoginResponseBody(
    val message: String,
    val token: String,
    val id: String,
    val response: UserNorm
)

data class PostsResponseBody(
    val response: List<PostDetails>
)

data class PostDetails(
    val _id: String,
    val name: String,
    val phone: String,
    val details: String,
    val location: String,
    val owner: String,
    val image: String,
    val createdAt: String,
    val updatedAt: String,
    val __v: Int
)

data class MakePostResponseBody(
    val message: String,
    val token: String,
    val id: String,
    val phone: String,
    val post: MakePostDetails
)

data class MakePostDetails(
    val _id: String,
    val name: String,
    val phone: String,
    val details: String,
    val location: String,
    val owner: String,
    val accepted: Boolean,
    val image: String,
    val createdAt: String,
    val updatedAt: String,
    val __v: Int
)

data class CreateVendorResponse(
    val message: String,
    val response: CVUserResponse
)

data class NotificationResponse(
    val error: Any? = null,
    val message: String? = null,
    val status: String? = null,
    val response: List<Notifications>? = null
)

data class CVUserResponse(
    val notifications: List<Notifications>,
    val _id: String,
    val name: String,
    val email: String,
    val phone: String,
    val password: String,
    val createdAt: String,
    val updatedAt: String,
    val src: String? = null,
    val __v: Int,
    val description: String,
    val isVendor: Boolean,
    val truck_plate_number: String,
    val truck_src: String
)

data class Notifications(
    val name: String,
    val image: String,
    val ID: String,
    val time: String,
    val details: String
)

data class UpdateResponse(
    val message: String,
    val response: UserResponse
)

data class AcceptOfferResponse(
    val status: String,
    val data: AcceptOfferResponseData,
    val message: String? = null
)

data class AcceptOfferResponseData(
    val _id: String,
    val name: String,
    val phone: String,
    val details: String,
    val location: String,
    val owner: String,
    val image: String,
    val createdAt: String,
    val updatedAt: String,
    val __v: Int
)

data class ForgotPasswordResponse(
    val id: String,
    val message: String
)

data class ChangePasswordResponse(
    val message: String,
    val response: UserNorm
)