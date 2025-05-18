package com.juan.ui.shared

import kotlinx.coroutines.flow.StateFlow

interface CitySelectionManager {
    val selectedCityId: StateFlow<Long?>
    fun selectCityId(city: Long?)
}