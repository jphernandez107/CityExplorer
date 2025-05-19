package com.juan.domain.usecase

import com.juan.domain.model.City

/**
 * Filters a list of cities based on prefix and favorite status.
 */
class FilterCitiesUseCase {

    /**
     * Applies filtering rules to the given list of cities.
     *
     * @param cities the full list of cities
     * @param prefix the prefix that city names must start with (case-sensitive)
     * @param onlyFavorites if true, filters to only favorite cities
     * @return a new list of filtered cities
     */
    operator fun invoke(
        cities: List<City>,
        prefix: String,
        onlyFavorites: Boolean,
    ): List<City> = cities
        .asSequence()
        .filter {
            if (onlyFavorites) {
                it.isFavorite
            } else {
                true
            }
        }
        .filter {
            it.name.startsWith(prefix)
        }
        .toList()
}