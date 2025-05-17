package com.juan.domain.usecase

import com.juan.domain.repository.CityRepository

class GetAllCitiesUseCase(
    private val cityRepository: CityRepository,
) {
    operator fun invoke() = cityRepository.getAllCities()
}