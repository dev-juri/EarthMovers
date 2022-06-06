package com.earthmovers.www.data.domain

data class RecentProject(
    val _id: String,
    val name: String,
    val phone: String,
    val projectHighlight: String,
    val location: String,
    val owner: String,
    val image: String,
    val createdAt: String,
    val updatedAt: String
)
