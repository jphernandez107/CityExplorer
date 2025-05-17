package com.juan.domain.usecase

import com.juan.domain.repository.CityRepository

class SearchCitiesUseCase(
    private val cityRepository: CityRepository,
) {
    operator fun invoke(query: String) = cityRepository.searchCities(query)
}