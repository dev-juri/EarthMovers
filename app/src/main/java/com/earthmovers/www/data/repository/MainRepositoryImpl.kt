package com.earthmovers.www.data.repository

import com.earthmovers.www.data.local.LocalDataSource
import com.earthmovers.www.data.remote.EarthMoversService
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class MainRepositoryImpl @Inject constructor(
    private val localSource: LocalDataSource,
    private val remoteSource: EarthMoversService,
    private val dispatcher: CoroutineDispatcher
) : MainRepository {
}