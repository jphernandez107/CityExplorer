package com.juan.domain.usecase

import com.juan.domain.repository.CityRepository

/**
 * Fetches city data from the remote API and caches it locally.
 *
 * This should be used on initial app launch or manual refresh events.
 *
 * @param cityRepository the repository coordinating API and local storage
 */
class FetchAndCacheCitiesUseCase(
    private val cityRepository: CityRepository,
) {
    /**
     * Initiates the fetch and cache process.
     *
     * @param forceRefresh if true, forces a network fetch even if cache is present
     * @return [Result.success] if the operation succeeds, [Result.failure] otherwise
     */
    suspend operator fun invoke(forceRefresh: Boolean = false): Result<Unit> =
        cityRepository.fetchAndCacheCities(forceRefresh)
}