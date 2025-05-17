package com.juan.domain.usecase

import com.juan.domain.repository.CityRepository

class UpdateCityFavoriteStatusUseCase(
    private val cityRepository: CityRepository,
) {
    suspend operator fun invoke(
        cityId: Long,
        isFavorite: Boolean,
    ) = cityRepository.updateCityFavoriteStatus(
        cityId = cityId,
        isFavorite = isFavorite,
    )
}