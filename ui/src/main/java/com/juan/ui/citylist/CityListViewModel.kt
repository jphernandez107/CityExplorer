package com.juan.ui.citylist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.juan.domain.model.City
import com.juan.domain.usecase.FetchAndCacheCitiesUseCase
import com.juan.domain.usecase.GetAllCitiesUseCase
import com.juan.domain.usecase.UpdateCityFavoriteStatusUseCase
import com.juan.ui.shared.CitySelectionManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CityListViewModel @Inject constructor(
    private val fetchAndCacheCitiesUseCase: FetchAndCacheCitiesUseCase,
    private val getAllCitiesUseCase: GetAllCitiesUseCase,
    private val updateCityFavoriteStatusUseCase: UpdateCityFavoriteStatusUseCase,
    private val citySelectionManager: CitySelectionManager,
) : ViewModel() {

    private val _viewState = MutableStateFlow<CityListViewState>(CityListViewState.Loading)
    val viewState = _viewState.asStateFlow()

    private val _searchBarViewState = MutableStateFlow(SearchBarViewState(""))
    val searchBarViewState = _searchBarViewState.asStateFlow()

    private val _navigationEvent = MutableSharedFlow<CityNavigationEvent>()
    val navigationEvent = _navigationEvent.asSharedFlow()

    init {
        fetchCitiesFromApiIfNeeded()
        observeSearchQuery()
    }

    private fun fetchCitiesFromApiIfNeeded() {
        viewModelScope.launch {
            _viewState.value = CityListViewState.Loading
            fetchAndCacheCitiesUseCase()
                .onFailure {
                    _viewState.value = CityListViewState.Error.Network
                }
        }
    }

    private fun observeSearchQuery() {
        combine(
            searchBarViewState,
            getAllCitiesUseCase(),
            citySelectionManager.selectedCityId,
        ) { searchBarViewState, cities, selectedCity ->
            val filteredCities = cities
                .filter {
                    if (searchBarViewState.onlyFavorites) {
                        it.isFavorite
                    } else {
                        true
                    }
                }
                .filter {
                    it.name.startsWith(searchBarViewState.searchQuery)
                }
            filteredCities to selectedCity
        }
        .onEach { (result, selectedCity) ->
            val cities = result.map { city ->
                city.toCityItemViewState(city.id == selectedCity)
            }
            _viewState.value = if (cities.isEmpty()) {
                CityListViewState.Empty
            } else {
                CityListViewState.Success(
                    cities = cities,
                )
            }
        }
        .catch { e ->
            e.printStackTrace()
            _viewState.value = CityListViewState.Error.General
        }.launchIn(viewModelScope)
    }

    fun onEvent(event: CityListUiEvent) {
        when(event) {
            is CityListUiEvent.OnSearchQueryChange -> {
                _searchBarViewState.update { it.copy(searchQuery = event.query) }
            }
            is CityListUiEvent.OnCityClick -> onCitySelected(event)
            is CityListUiEvent.OnCityFavoriteClick -> {
                setCityFavoriteStateToLoading(event.cityId)
                viewModelScope.launch {
                    updateCityFavoriteStatusUseCase(
                        cityId = event.cityId,
                        isFavorite = !event.favoriteState.toBoolean(),
                    )
                }
            }
            is CityListUiEvent.OnRefresh -> {
                fetchCitiesFromApiIfNeeded()
            }
            is CityListUiEvent.OnOnlyFavoritesClick -> {
                _searchBarViewState.update { it.copy(onlyFavorites = !it.onlyFavorites) }
            }
            is CityListUiEvent.OnCityDetailsClick -> {
                viewModelScope.launch {
                    _navigationEvent.emit(
                        CityNavigationEvent.NavigateToCityDetails(
                            cityId = event.cityId,
                        )
                    )
                }
            }
        }
    }

    private fun onCitySelected(event: CityListUiEvent.OnCityClick) {
        citySelectionManager.selectCityId(event.cityId)
        viewModelScope.launch {
            _navigationEvent.emit(
                CityNavigationEvent.NavigateToMapIfPortrait(
                    cityId = event.cityId,
                )
            )
        }
    }

    private fun setCityFavoriteStateToLoading(cityId: Long) {
        _viewState.update {
            when (val state = it) {
                is CityListViewState.Success -> state.copy(
                    cities = state.cities.map { city ->
                        if (city.id == cityId) {
                            city.copy(favoriteState = FavoriteState.Loading)
                        } else {
                            city
                        }
                    }
                )
                else -> _viewState.value
            }
        }
    }

    private fun City.toCityItemViewState(isSelected: Boolean) = CityItemViewState(
        id = id,
        name = name,
        country = country,
        favoriteState = FavoriteState.from(isFavorite),
        coordinates = coordinatesString,
        isSelected = isSelected,
    )
}