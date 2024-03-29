package com.earthmovers.app.data.mapper

import com.earthmovers.app.data.domain.DomainNotification
import com.earthmovers.app.data.domain.RecentProject
import com.earthmovers.app.data.domain.User
import com.earthmovers.app.data.local.entity.DbNotification
import com.earthmovers.app.data.local.entity.DbRecentPost
import com.earthmovers.app.data.local.entity.DbUser
import com.earthmovers.app.data.remote.LoginResponseBody
import com.earthmovers.app.data.remote.NotificationResponse
import com.earthmovers.app.data.remote.PostsResponseBody
import com.earthmovers.app.data.remote.RegisterResponseBody

fun RegisterResponseBody.toDatabaseModel(): DbUser {
    return this.let {
        DbUser(
            token = it.token,
            id = it.user._id,
            name = it.user.name,
            email = it.user.email,
            phone = it.user.phone,
            src = it.user.src,
            description = it.user.description,
            isVendor = it.user.isVendor,
            truck_plate_number = it.user.truck_plate_number,
            truck_src = it.user.truck_src
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
            email = it.email,
            src = it.src,
            description = it.description,
            isVendor = it.isVendor,
            truck_plate_number = it.truck_plate_number,
            truck_src = it.truck_src
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
            email = it.response.email,
            src = it.response.src,
            description = it.response.description,
            isVendor = it.response.isVendor,
            truck_plate_number = it.response.truck_plate_number,
            truck_src = it.response.truck_src
        )
    }
}

fun PostsResponseBody.toDbModel(): Array<DbRecentPost> {
    return response.map {
        DbRecentPost(
            id = it._id,
            name = it.name,
            phone = it.phone,
            projectHighlight = it.details,
            location = it.location,
            owner = it.owner,
            image = it.image,
            createdAt = it.createdAt,
            updatedAt = it.updatedAt
        )
    }.toTypedArray()
}

fun List<DbRecentPost>.toDomainPost(): List<RecentProject> {
    return map {
        RecentProject(
            _id = it.id,
            name = it.name,
            phone = it.phone,
            projectHighlight = it.projectHighlight,
            location = it.location,
            owner = it.owner,
            image = it.image,
            createdAt = it.createdAt,
            updatedAt = it.updatedAt
        )
    }
}

fun DbRecentPost.toDomainModel(): RecentProject {
    return RecentProject(
        _id = this.id,
        name = this.name,
        phone = this.phone,
        projectHighlight = this.projectHighlight,
        location = this.location,
        owner = this.owner,
        image = this.image,
        createdAt = this.createdAt,
        updatedAt = this.updatedAt
    )
}

fun List<DbNotification>.toDomainNotification(): List<DomainNotification> {
    return map {
        DomainNotification(
            name = it.name,
            image = it.image,
            ID = it.ID,
            time = it.time,
            details = it.details
        )
    }
}

fun NotificationResponse.toDbModel(): Array<DbNotification> {
    return response!!.map {
        DbNotification(
            name = it.name,
            image = it.image,
            ID = it.ID,
            time = it.time,
            details = it.details
        )
    }.toTypedArray()
}


