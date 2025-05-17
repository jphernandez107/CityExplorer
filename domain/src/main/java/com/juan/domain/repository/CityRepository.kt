package com.juan.domain.repository

import com.juan.domain.model.City
import kotlinx.coroutines.flow.Flow

interface CityRepository {
    suspend fun fetchAndCacheCities(forceRefresh: Boolean = false): Result<Unit>
    fun getAllCities(): Flow<List<City>>
    suspend fun updateCityFavoriteStatus(
        cityId: Long,
        isFavorite: Boolean,
    ): Boolean
}