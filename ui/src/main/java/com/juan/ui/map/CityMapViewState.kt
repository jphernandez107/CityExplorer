package com.juan.ui.map

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import com.juan.domain.model.City

@Stable
sealed interface CityMapViewState {

    @Immutable
    data object Loading : CityMapViewState

    @Immutable
    data object Error : CityMapViewState

    @Immutable
    data class Success(
        val city: City,
    ) : CityMapViewState
}