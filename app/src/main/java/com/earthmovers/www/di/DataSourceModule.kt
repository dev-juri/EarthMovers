package com.earthmovers.www.di

import com.earthmovers.www.data.local.LocalDataSource
import com.earthmovers.www.data.local.LocalDataSourceImpl
import com.earthmovers.www.data.repository.MainRepository
import com.earthmovers.www.data.repository.MainRepositoryImpl
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
    abstract fun bindLocalSource(
        localDataSourceImpl: LocalDataSourceImpl
    ): LocalDataSource

    @Singleton
    @Binds
    abstract fun bindRepository(
        mainRepositoryImpl: MainRepositoryImpl
    ): MainRepository
}