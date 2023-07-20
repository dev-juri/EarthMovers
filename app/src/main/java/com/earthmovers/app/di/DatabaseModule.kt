package com.earthmovers.app.di

import android.app.Application
import androidx.room.Room
import com.earthmovers.app.data.local.EarthMoversDatabase
import com.earthmovers.app.data.local.dao.EarthMoversDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(application: Application): EarthMoversDatabase {
        return Room.databaseBuilder(application, EarthMoversDatabase::class.java, "EarthMovers.db")
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun provideEarthMoversDao(database: EarthMoversDatabase): EarthMoversDao {
        return database.earthMoversDao
    }

}

