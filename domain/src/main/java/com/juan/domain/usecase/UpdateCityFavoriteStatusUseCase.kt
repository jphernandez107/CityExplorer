package com.juan.domain.usecase

import com.juan.domain.repository.CityRepository

/**
 * Updates the favorite status of a specific city in the repository.
 *
 * This is typically triggered by user interactions with the UI to toggle a city's favorite state.
 *
 * @param cityRepository the repository handling the update logic
 */
class UpdateCityFavoriteStatusUseCase(
    private val cityRepository: CityRepository,
) {
    /**
     * Executes the update operation.
     *
     * @param cityId the ID of the city to update
     * @param isFavorite the desired favorite status
     * @return true if the update was successful
     */
    suspend operator fun invoke(
        cityId: Long,
        isFavorite: Boolean,
    ): Boolean = cityRepository.updateCityFavoriteStatus(
        cityId = cityId,
        isFavorite = isFavorite,
    )
}