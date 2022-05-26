package com.earthmovers.www.di

import com.earthmovers.www.data.local.LocalDataSource
import com.earthmovers.www.data.local.LocalDataSourceImpl
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

}