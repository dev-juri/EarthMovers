package com.earthmovers.www.data.local

import com.earthmovers.www.data.local.dao.EarthMoversDao
import javax.inject.Inject

class LocalDataSourceImpl @Inject constructor(private val earthMoversDao: EarthMoversDao) :
    LocalDataSource {
  }