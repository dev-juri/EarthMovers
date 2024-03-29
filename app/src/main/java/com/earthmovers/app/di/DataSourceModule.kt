package com.earthmovers.app.di

import com.earthmovers.app.data.repository.MainRepository
import com.earthmovers.app.data.repository.MainRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataSourceModule {
    @Singleton
    @Binds
    abstract fun bindRepository(
        mainRepositoryImpl: MainRepositoryImpl
    ): MainRepository
}