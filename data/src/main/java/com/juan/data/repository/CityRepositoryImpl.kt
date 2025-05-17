package com.juan.data.repository

import com.juan.data.mapper.toEntity
import com.juan.data.service.CityApi
import com.juan.db.dao.CityDao
import com.juan.db.entity.CityEntity
import com.juan.db.mapper.toDomain
import com.juan.domain.repository.CityRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class CityRepositoryImpl(
    private val api: CityApi,
    private val cityDao: CityDao,
) : CityRepository {

    override suspend fun fetchAndCacheCities(forceRefresh: Boolean) = try {
        val hasCachedValues = cityDao.hasCities()
        if (!hasCachedValues || forceRefresh) {
            val response = api.getCities()
            val entities = response.map { it.toEntity() }
            cityDao.insertCities(entities)
        }
        Result.success(Unit)
    } catch (e: Exception) {
        e.printStackTrace()
        Result.failure(e)
    }

    override fun getAllCities() =
        cityDao.getAllCities().toDomainFlow()

    override suspend fun updateCityFavoriteStatus(
        cityId: Long,
        isFavorite: Boolean,
    ) = cityDao.updateCityFavoriteStatus(
        cityId = cityId,
        isFavorite = isFavorite,
    ) > 0

    private fun Flow<List<CityEntity>>.toDomainFlow() =
        map { list ->
            list.map { it.toDomain() }
        }
}