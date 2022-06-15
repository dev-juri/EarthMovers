package com.earthmovers.www.data.remote

import com.squareup.moshi.Json

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
    val status: String,
    val response: List<Notifications>
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
    val data: AcceptOfferResponseData
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

data class DirectionResponseModel(
    @field:Json(name = "routes")
    var directionRouteModels: List<DirectionRouteModel>? = null,

    @field:Json(name = "error_message")
    val error: String? = null
)

data class DirectionRouteModel(
    @field:Json(name = "legs")
    var legs: List<DirectionLegModel>? = null,

    @field:Json(name = "overview_polyline")
    var polylineModel: DirectionPolylineModel? = null,

    @field:Json(name = "summary")
    var summary: String? = null
)

data class DirectionLegModel(
    @field:Json(name = "distance")
    val distance: DirectionDistanceModel? = null,

    @field:Json(name = "duration")
    val duration: DirectionDurationModel? = null,

    @field:Json(name = "end_address")
    val endAddress: String? = null,

    @field:Json(name = "end_location")
    val endLocation: EndLocationModel? = null,

    @field:Json(name = "start_address")
    val startAddress: String? = null,

    @field:Json(name = "start_location")
    val startLocation: StartLocationModel? = null,

    @field:Json(name = "steps")
    val steps: List<DirectionStepModel>? = null
)

data class DirectionPolylineModel(
    @field:Json(name = "points")
    var points: String? = null
)

data class DirectionDurationModel(
    @field:Json(name = "text")
    var text: String? = null,

    @field:Json(name = "value")
    var value: Int? = null
)

data class EndLocationModel(
    @field:Json(name="lat")
    var lat: Double? = null,

    @field:Json(name="lng")
    var lng: Double? = null
)

data class StartLocationModel(
    @field:Json(name="lat")
    var lat: Double? = null,

    @field:Json(name="lng")
    var lng: Double? = null
)

data class DirectionStepModel(
    @field:Json(name = "distance")
    val distance: DirectionDistanceModel? = null,

    @field:Json(name = "duration")
    val duration: DirectionDurationModel? = null,

    @field:Json(name = "end_location")
    val endLocation: EndLocationModel? = null,

    @field:Json(name = "html_instructions")
    val htmlInstructions: String? = null,

    @field:Json(name = "polyline")
    val polyline: DirectionPolylineModel? = null,

    @field:Json(name = "start_location")
    val startLocation: StartLocationModel? = null,

    @field:Json(name = "travel_mode")
    val travelMode: String? = null,

    @field:Json(name = "manuever")
    val maneuver: String? = null

)

data class DirectionDistanceModel(
    @field:Json(name = "text")
    var text: String? = null,

    @field:Json(name = "value")
    var value: Int? = null
)
