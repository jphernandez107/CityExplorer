package com.juan.ui.citylist

sealed interface CityNavigationEvent {
    data class NavigateToMap(
        val cityId: Long,
    ) : CityNavigationEvent
    data class NavigateToCityDetails(
        val cityId: Long,
    ) : CityNavigationEvent
}