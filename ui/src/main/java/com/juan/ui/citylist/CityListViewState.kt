package com.juan.ui.citylist

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable

@Stable
sealed interface CityListViewState {
    @Stable
    sealed interface Loading: CityListViewState {
        @Immutable
        data object Network : Loading

        @Immutable
        data object Local : Loading
    }

    @Immutable
    data object Empty : CityListViewState

    @Stable
    sealed interface Error : CityListViewState {
        @Immutable
        data object Network : Error

        @Immutable
        data object General : Error
    }

    @Immutable
    data class Success(
        val cities: List<CityItemViewState>,
    ) : CityListViewState
}

@Immutable
data class CityItemViewState(
    val id: Long,
    val name: String,
    val country: String,
    val coordinates: String,
    val favoriteState: FavoriteState,
    val isSelected: Boolean = false,
)
