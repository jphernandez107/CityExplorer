package com.juan.domain.usecase

import com.juan.domain.model.City
import com.juan.domain.repository.CityRepository

class AddCityToFavoritesUseCase(
    private val cityRepository: CityRepository,
) {
    suspend operator fun invoke(city: City) = cityRepository.addCityToFavorites(city)
}