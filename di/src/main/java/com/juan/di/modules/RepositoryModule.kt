package com.juan.di.modules

import com.juan.data.repository.CityRepositoryImpl
import com.juan.data.service.CityApi
import com.juan.db.dao.CityDao
import com.juan.db.dao.FavoriteCityDao
import com.juan.domain.repository.CityRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideCityRepository(
        cityApi: CityApi,
        cityDao: CityDao,
        favoriteCityDao: FavoriteCityDao,
    ): CityRepository = CityRepositoryImpl(
        api = cityApi,
        cityDao = cityDao,
        favoriteCityDao = favoriteCityDao,
    )
}