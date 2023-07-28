package com.earthmovers.app.data.local.dao


import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.earthmovers.app.data.local.entity.DbNotification
import com.earthmovers.app.data.local.entity.DbRecentPost
import com.earthmovers.app.data.local.entity.DbUser

@Dao
interface EarthMoversDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveLoggedUserInfo(vararg user: DbUser)

    @Query("SELECT * from User LIMIT 1")
    fun getLoggedUserInfo(): LiveData<DbUser?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveRecentPosts(vararg posts: DbRecentPost)

    @Query("SELECT * from RecentPosts ORDER by updatedAt DESC")
    fun fetchAllRecentPosts(): LiveData<List<DbRecentPost>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveNotifications(vararg notifications: DbNotification)

    @Query("SELECT * from Notifications ORDER by time DESC")
    fun getAllNotifications(): LiveData<List<DbNotification>>

    @Query("SELECT * from RecentPosts WHERE id=:id")
    fun getPostDetails(id: String): LiveData<DbRecentPost>

    @Query("DELETE from RecentPosts")
    suspend fun deleteAllPosts()

    @Query("DELETE from Notifications")
    suspend fun deleteAllNotifications()
}
