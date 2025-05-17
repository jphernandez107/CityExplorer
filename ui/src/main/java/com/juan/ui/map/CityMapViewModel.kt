package com.juan.ui.map

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.juan.domain.usecase.GetCityByIdUseCase
import com.juan.ui.navigation.Screen.Companion.CITY_ID_KEY
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CityMapViewModel  @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getCityByIdUseCase: GetCityByIdUseCase,
) : ViewModel() {
    private val cityId: Long = savedStateHandle.get<String>(CITY_ID_KEY)?.toLongOrNull() ?: -1L

    private val _viewState = MutableStateFlow<CityMapViewState>(CityMapViewState.Loading)
    val viewState = _viewState.asStateFlow()

    init {
        fetchCity()
    }

    private fun fetchCity() {
        if (cityId == -1L) {
            _viewState.value = CityMapViewState.Error
            return
        }

        viewModelScope.launch {
            getCityByIdUseCase(cityId).collectLatest { city ->
                if (city != null) {
                    _viewState.update {
                        CityMapViewState.Success(city)
                    }
                } else {
                    _viewState.update {
                        CityMapViewState.Error
                    }
                }
            }
        }
    }
}