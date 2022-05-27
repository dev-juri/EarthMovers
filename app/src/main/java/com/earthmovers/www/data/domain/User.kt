package com.earthmovers.www.data.domain

data class User(
    val token: String,
    val id: String,
    val phone: String,
    val email: String,
    val name: String
)
