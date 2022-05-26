package com.earthmovers.www.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class DbNotification(
    @PrimaryKey
    var details: String,
    var time: String
)