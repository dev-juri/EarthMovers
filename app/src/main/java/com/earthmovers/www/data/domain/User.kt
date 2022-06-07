package com.earthmovers.www.data.domain

data class User(
    val token: String,
    val id: String,
    val phone: String,
    val email: String,
    val name: String,
    val description: String? = null,
    val isVendor: Boolean? = false,
    val truck_plate_number: String? = null,
    val truck_src: String? = null
)
