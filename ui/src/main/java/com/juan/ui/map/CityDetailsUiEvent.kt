package com.juan.ui.map

import com.juan.ui.citylist.FavoriteState

sealed interface CityDetailsUiEvent {
    data class OnFavoriteClick(val favoriteState: FavoriteState) : CityDetailsUiEvent
}