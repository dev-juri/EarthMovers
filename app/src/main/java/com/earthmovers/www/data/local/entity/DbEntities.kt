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
    val name: String
)

@Entity(tableName = "RecentPosts")
data class DbRecentPost(
    @PrimaryKey(autoGenerate = false)
    val id: String,
    val name: String,
    val src: String,
    val phone: String,
    val projectHighlight: String,
    val location: String,
    val owner: String,
    val image: String,
    val createdAt: String,
    val updatedAt: String
)
