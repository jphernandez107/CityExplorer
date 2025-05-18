package com.juan.ui.shared

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

class CitySelectionManagerImpl @Inject constructor() : CitySelectionManager {
    private val _selectedCityId = MutableStateFlow<Long?>(null)
    override val selectedCityId: StateFlow<Long?> = _selectedCityId.asStateFlow()

    override fun selectCityId(city: Long?) {
        _selectedCityId.value = city
    }
}