package com.juan.domain.usecase

import com.juan.domain.model.City

class FilterCitiesUseCase {
    operator fun invoke(
        cities: List<City>,
        prefix: String,
        onlyFavorites: Boolean,
    ) = cities
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