package com.juan.ui.citylist

sealed interface CityNavigationEvent {
    data class NavigateToMapIfPortrait(
        val cityId: Long,
    ) : CityNavigationEvent
    data class NavigateToCityDetails(
        val cityId: Long,
    ) : CityNavigationEvent
}