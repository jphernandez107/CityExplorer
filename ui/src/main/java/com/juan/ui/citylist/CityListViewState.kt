package com.juan.ui.citylist

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable

@Stable
sealed interface CityListViewState {
    @Immutable
    data object Loading: CityListViewState

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
) {
    @Stable
    sealed interface FavoriteState {
        @Immutable
        data object Favorite : FavoriteState

        @Immutable
        data object NotFavorite : FavoriteState

        @Immutable
        data object Loading : FavoriteState

        fun toBoolean(): Boolean {
            return when (this) {
                is Favorite -> true
                is NotFavorite -> false
                is Loading -> false
            }
        }

        companion object {
            fun from(isFavorite: Boolean) = if (isFavorite) {
                Favorite
            } else {
                NotFavorite
            }
        }
    }
}