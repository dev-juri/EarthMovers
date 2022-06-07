package com.earthmovers.www.data.local.dao


import androidx.lifecycle.LiveData
import androidx.room.*
import com.earthmovers.www.data.local.entity.DbNotification
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

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveNotifications(vararg notifications: DbNotification)

    @Query("SELECT * from Notifications ORDER by time")
    fun getAllNotifications(): LiveData<List<DbNotification>>

    @Query("SELECT * from RecentPosts WHERE id=:id")
    fun getPostDetails(id: String): LiveData<DbRecentPost>

    @Query("DELETE from RecentPosts")
    suspend fun deleteAllPosts()
}
