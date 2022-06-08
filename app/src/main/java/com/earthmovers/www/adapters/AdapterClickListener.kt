package com.earthmovers.www.adapters

import com.earthmovers.www.data.domain.RecentProject

interface PostClickListener {
    fun onPostClick(project: RecentProject)
}

interface NotificationClickListener {
    fun checkProfile(id: String)
}