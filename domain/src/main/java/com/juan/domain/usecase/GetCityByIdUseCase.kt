package com.juan.domain.usecase

import com.juan.domain.repository.CityRepository

/**
 * Retrieves a city by its unique ID.
 */
class GetCityByIdUseCase(
    private val cityRepository: CityRepository,
) {
    operator fun invoke(id: Long) = cityRepository.getCityById(id)
}