package com.juan.di.modules

import com.juan.domain.repository.CityRepository
import com.juan.domain.usecase.FetchAndCacheCitiesUseCase
import com.juan.domain.usecase.FilterCitiesUseCase
import com.juan.domain.usecase.GetAllCitiesUseCase
import com.juan.domain.usecase.GetCityByIdUseCase
import com.juan.domain.usecase.UpdateCityFavoriteStatusUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {

    @Provides
    fun provideFetchCitiesUseCase(
        cityRepository: CityRepository
    ): FetchAndCacheCitiesUseCase = FetchAndCacheCitiesUseCase(cityRepository)

    @Provides
    fun provideGetAllCitiesUseCase(
        cityRepository: CityRepository
    ): GetAllCitiesUseCase = GetAllCitiesUseCase(cityRepository)

    @Provides
    fun provideUpdateCityFavoriteStatusUseCase(
        cityRepository: CityRepository
    ): UpdateCityFavoriteStatusUseCase = UpdateCityFavoriteStatusUseCase(cityRepository)

    @Provides
    fun provideGetCityByIdUseCase(
        cityRepository: CityRepository
    ): GetCityByIdUseCase = GetCityByIdUseCase(cityRepository)

    @Provides
    fun provideFilterCitiesUseCase(): FilterCitiesUseCase =
        FilterCitiesUseCase()
}