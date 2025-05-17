package com.juan.ui.details

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import com.juan.ui.citylist.FavoriteState

@Stable
sealed interface CityDetailsViewState {
    @Immutable
    data object Loading : CityDetailsViewState

    @Immutable
    data object Error : CityDetailsViewState

    @Immutable
    data class Success(
        val cityId: Long,
        val name: String,
        val country: String,
        val coordinates: String,
        val latitude: Double,
        val longitude: Double,
        val favoriteState: FavoriteState,
    ) : CityDetailsViewState
}