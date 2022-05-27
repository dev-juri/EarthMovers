package com.earthmovers.www.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.earthmovers.www.data.local.dao.EarthMoversDao
import com.earthmovers.www.data.local.entity.DbUser

@Database(
    entities = [DbUser::class],
    version = 1,
    exportSchema = true
)
abstract class EarthMoversDatabase : RoomDatabase() {
    abstract val earthMoversDao: EarthMoversDao
}
