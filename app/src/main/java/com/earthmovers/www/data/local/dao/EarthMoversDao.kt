package com.earthmovers.www.data.local.dao


import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.earthmovers.www.data.local.entity.DbRecentPost
import com.earthmovers.www.data.local.entity.DbUser

@Dao
interface EarthMoversDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveLoggedUserInfo(vararg user: DbUser)

    @Query("SELECT * from User LIMIT 1")
    fun getLoggedUserInfo(): LiveData<DbUser?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveRecentPosts(vararg posts: DbRecentPost)

    @Query("SELECT * from RecentPosts ORDER by updatedAt")
    fun fetchAllRecentPosts(): LiveData<List<DbRecentPost>>
}
