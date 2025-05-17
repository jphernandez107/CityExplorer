package com.juan.domain.usecase

import com.juan.domain.repository.CityRepository

class FetchCitiesUseCase(
    private val cityRepository: CityRepository,
) {
    suspend operator fun invoke(forceRefresh: Boolean = false) =
        cityRepository.fetchCities(forceRefresh)
}