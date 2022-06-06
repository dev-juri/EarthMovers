package com.earthmovers.www.di


import com.earthmovers.www.data.local.dao.EarthMoversDao
import com.earthmovers.www.data.remote.EarthMoversService
import com.earthmovers.www.data.repository.MainRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Singleton


@InstallIn(SingletonComponent::class)
@Module
object RepositoryModule {

    @Singleton
    @Provides
    fun provideMainRepository(
        localSource: EarthMoversDao,
        remoteSource: EarthMoversService,
        dispatcher: CoroutineDispatcher
    ): MainRepositoryImpl = MainRepositoryImpl(localSource, remoteSource, dispatcher)

}