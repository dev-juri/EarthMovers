package com.earthmovers.app.adapters

import com.earthmovers.app.data.domain.RecentProject

interface PostClickListener {
    fun onPostClick(project: RecentProject)
}

interface NotificationClickListener {
    fun checkProfile(id: String)
}