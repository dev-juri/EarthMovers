package com.earthmovers.www.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "User")
data class DbUser(
    val token: String,
    @PrimaryKey(autoGenerate = false)
    val id: String,
    val phone: String,
    val email: String,
    val name: String,
    val description: String? = null,
    val isVendor: Boolean? = false,
    val truck_plate_number: String? = null,
    val truck_src: String? = null
)

@Entity(tableName = "RecentPosts")
data class DbRecentPost(
    @PrimaryKey(autoGenerate = false)
    val id: String,
    val name: String,
    val phone: String,
    val projectHighlight: String,
    val location: String,
    val owner: String,
    val image: String,
    val createdAt: String,
    val updatedAt: String
)

@Entity(tableName = "Notifications")
data class DbNotification(
    val name: String,
    val image: String,
    @PrimaryKey(autoGenerate = false)
    val ID: String,
    val time: String,
    val details: String
)
