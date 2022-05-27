package com.earthmovers.www.data.mapper

import com.earthmovers.www.data.domain.User
import com.earthmovers.www.data.local.entity.DbUser
import com.earthmovers.www.data.remote.LoginResponseBody
import com.earthmovers.www.data.remote.RegisterResponseBody

fun RegisterResponseBody.toDatabaseModel(): DbUser {
    return this.let {
        DbUser(
            token = it.token,
            id = it.user._id,
            name = it.user.name,
            email = it.user.email,
            phone = it.user.phone
        )
    }
}

fun DbUser?.toDomainUSer(): User? {
    return this?.let {
        User(
            token = it.token,
            id = it.id,
            name = it.name,
            phone = it.phone,
            email = it.email
        )
    }
}

fun LoginResponseBody.toDatabaseModel(): DbUser {
    return this.let {
        DbUser(
            token = it.token,
            id = it.id,
            name = it.response.name,
            phone = it.response.phone,
            email = it.response.email
        )
    }
}