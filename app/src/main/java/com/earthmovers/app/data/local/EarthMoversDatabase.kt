package com.earthmovers.app.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.earthmovers.app.data.local.dao.EarthMoversDao
import com.earthmovers.app.data.local.entity.DbNotification
import com.earthmovers.app.data.local.entity.DbRecentPost
import com.earthmovers.app.data.local.entity.DbUser

@Database(
    entities = [DbUser::class, DbRecentPost::class, DbNotification::class],
    version = 4,
    exportSchema = false
)
abstract class EarthMoversDatabase : RoomDatabase() {
    abstract val earthMoversDao: EarthMoversDao
}
