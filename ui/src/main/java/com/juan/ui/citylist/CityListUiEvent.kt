package com.juan.ui.citylist

sealed interface CityListUiEvent {
    data class OnSearchQueryChange(val query: String) : CityListUiEvent
    data class OnCityClick(val cityId: Long) : CityListUiEvent
    data class OnCityFavoriteClick(
        val cityId: Long,
        val favoriteState: CityItemViewState.FavoriteState,
    ) : CityListUiEvent
    data object OnRefresh : CityListUiEvent
    data object OnOnlyFavoritesClick : CityListUiEvent
}