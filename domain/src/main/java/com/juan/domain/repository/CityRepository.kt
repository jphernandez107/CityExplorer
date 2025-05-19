package com.juan.domain.repository

import com.juan.domain.model.City
import kotlinx.coroutines.flow.Flow

/**
 * Contract for accessing and manipulating city data from both local and remote sources.
 */
interface CityRepository {

    /**
     * Downloads city data from the remote API and stores it locally.
     *
     * @param forceRefresh if true, fetches from the API even if the local cache isn't empty.
     * @return [Result.success] on successful sync, [Result.failure] on error.
     */
    suspend fun fetchAndCacheCities(forceRefresh: Boolean = false): Result<Unit>

    /**
     * Observes the entire list of cities stored locally.
     *
     * @return a cold [Flow] emitting all cities from the database.
     */
    fun getAllCities(): Flow<List<City>>

    /**
     * Updates the favorite status of a specific city.
     *
     * @param cityId the ID of the city to update
     * @param isFavorite the new favorite status
     * @return true if the update was successful
     */
    suspend fun updateCityFavoriteStatus(
        cityId: Long,
        isFavorite: Boolean,
    ): Boolean

    /**
     * Retrieves a single city by ID.
     *
     * @param id the ID of the city
     * @return a [Flow] that emits the city or null if not found
     */
    fun getCityById(id: Long): Flow<City?>
}