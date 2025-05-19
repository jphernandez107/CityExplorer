package com.juan.domain.usecase

import com.juan.domain.repository.CityRepository

/**
 * Retrieves all cities stored locally.
 *
 * Used by UI to observe all current city data from the database.
 */
class GetAllCitiesUseCase(
    private val cityRepository: CityRepository,
) {
    operator fun invoke() = cityRepository.getAllCities()
}