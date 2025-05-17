package com.juan.ui.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.juan.domain.model.City
import com.juan.domain.usecase.GetCityByIdUseCase
import com.juan.domain.usecase.UpdateCityFavoriteStatusUseCase
import com.juan.ui.citylist.FavoriteState
import com.juan.ui.map.CityDetailsUiEvent
import com.juan.ui.navigation.Screen.Companion.CITY_ID_KEY
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CityDetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getCityByIdUseCase: GetCityByIdUseCase,
    private val updateCityFavoriteStatusUseCase: UpdateCityFavoriteStatusUseCase,
) : ViewModel() {

    private val cityId: Long = savedStateHandle.get<String>(CITY_ID_KEY)?.toLongOrNull() ?: -1L

    private val _viewState = MutableStateFlow<CityDetailsViewState>(CityDetailsViewState.Loading)
    val viewState = _viewState.asStateFlow()

    val screenTitle = viewState
        .map {
            when (it) {
                is CityDetailsViewState.Loading -> ""
                is CityDetailsViewState.Error -> "Error"
                is CityDetailsViewState.Success -> "${it.name}, ${it.country}"
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = WhileSubscribed(5_000),
            initialValue = ""
        )

    init {
        fetchCity()
    }

    private fun fetchCity() {
        if (cityId == -1L) {
            _viewState.value = CityDetailsViewState.Error
            return
        }

        viewModelScope.launch {
            getCityByIdUseCase(cityId).collect { city ->
                if (city == null) {
                    _viewState.value = CityDetailsViewState.Error
                } else {
                    _viewState.value = city.toCityDetailsViewState()
                }
            }
        }
    }

    fun onEvent(event: CityDetailsUiEvent) {
        when (event) {
            is CityDetailsUiEvent.OnFavoriteClick -> {
                setCityFavoriteStateToLoading()
                viewModelScope.launch {
                    updateCityFavoriteStatusUseCase(
                        cityId = cityId,
                        isFavorite = !event.favoriteState.toBoolean(),
                    )
                }
            }
        }
    }

    private fun setCityFavoriteStateToLoading() {
        when (val state = viewState.value) {
            is CityDetailsViewState.Success ->
                _viewState.value = state.copy(favoriteState = FavoriteState.Loading)
            is CityDetailsViewState.Loading,
            is CityDetailsViewState.Error -> Unit
        }
    }

    private fun City.toCityDetailsViewState() = CityDetailsViewState.Success(
        cityId = id,
        name = name,
        country = country,
        coordinates = coordinatesString,
        favoriteState = FavoriteState.from(isFavorite),
        latitude = geoCoordinates.latitude,
        longitude = geoCoordinates.longitude,
    )
}