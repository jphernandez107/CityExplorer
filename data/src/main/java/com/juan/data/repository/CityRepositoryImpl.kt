package com.juan.data.repository

import com.juan.data.mapper.toEntity
import com.juan.data.service.CityApi
import com.juan.db.dao.CityDao
import com.juan.db.dao.FavoriteCityDao
import com.juan.db.entity.CityEntity
import com.juan.db.entity.FavoriteCityEntity
import com.juan.db.mapper.toDomain
import com.juan.domain.model.City
import com.juan.domain.repository.CityRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map

class CityRepositoryImpl(
    private val api: CityApi,
    private val cityDao: CityDao,
    private val favoriteCityDao: FavoriteCityDao,
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

    override fun getAllCities(): Flow<List<City>> =
        combine(
            cityDao.getAllCities(),
            favoriteCityDao.getAllFavorites(),
        ) { cityEntities, favoriteEntities ->
            val favoriteMap = favoriteEntities.associateBy { it.cityId }
            cityEntities.map { entity ->
                val isFavorite = favoriteMap[entity.id]?.isFavorite ?: false
                entity.toDomain(isFavorite)
            }
        }

    override suspend fun updateCityFavoriteStatus(
        cityId: Long,
        isFavorite: Boolean,
    ): Boolean = try {
        favoriteCityDao.insertFavoriteCity(
            favorite = FavoriteCityEntity(
                cityId = cityId,
                isFavorite = isFavorite,
            )
        )
        true
    } catch (e: Exception) {
        e.printStackTrace()
        false
    }

    override fun getCityById(id: Long): Flow<City?> =
        combine(
            cityDao.getCityById(id),
            favoriteCityDao.getFavoriteByCityId(id),
        ) { cityEntity, favoriteCityEntity ->
            cityEntity?.toDomain(favoriteCityEntity?.isFavorite ?: false)
        }

    private fun Flow<List<CityEntity>>.toDomainFlow(isFavorite: Boolean) =
        map { list ->
            list.map { it.toDomain(isFavorite) }
        }
}