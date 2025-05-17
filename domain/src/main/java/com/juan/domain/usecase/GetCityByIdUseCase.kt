package com.juan.domain.usecase

import com.juan.domain.repository.CityRepository

class GetCityByIdUseCase(
    private val cityRepository: CityRepository,
) {
    operator fun invoke(id: Long) = cityRepository.getCityById(id)
}