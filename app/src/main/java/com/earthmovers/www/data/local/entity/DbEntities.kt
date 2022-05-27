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