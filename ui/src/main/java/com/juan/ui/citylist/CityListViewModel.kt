package com.juan.ui.citylist

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.juan.domain.model.City
import com.juan.domain.usecase.FetchAndCacheCitiesUseCase
import com.juan.domain.usecase.FilterCitiesUseCase
import com.juan.domain.usecase.GetAllCitiesUseCase
import com.juan.domain.usecase.UpdateCityFavoriteStatusUseCase
import com.juan.ui.shared.CitySelectionManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
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
import kotlinx.coroutines.plus
import javax.inject.Inject

@HiltViewModel
class CityListViewModel @Inject constructor(
    private val fetchAndCacheCitiesUseCase: FetchAndCacheCitiesUseCase,
    private val getAllCitiesUseCase: GetAllCitiesUseCase,
    private val updateCityFavoriteStatusUseCase: UpdateCityFavoriteStatusUseCase,
    private val filterCitiesUseCase: FilterCitiesUseCase,
    private val citySelectionManager: CitySelectionManager,
    private val ioDispatcher: CoroutineDispatcher,
) : ViewModel() {

    private val _viewState = MutableStateFlow<CityListViewState>(CityListViewState.Loading.Network)
    val viewState = _viewState.asStateFlow()

    private val _searchBarViewState = MutableStateFlow(SearchBarViewState(""))
    val searchBarViewState = _searchBarViewState.asStateFlow()

    private val _navigationEvent = MutableSharedFlow<CityNavigationEvent>()
    val navigationEvent = _navigationEvent.asSharedFlow()

    @VisibleForTesting
    internal val onRefreshFinished = MutableSharedFlow<Unit>(replay = 1)

    init {
        observeSearchQuery()
        fetchCitiesFromApiIfNeeded()
    }

    private fun fetchCitiesFromApiIfNeeded(forceRefresh: Boolean = false) {
        viewModelScope.launch(ioDispatcher) {
            _viewState.value = CityListViewState.Loading.Network
            fetchAndCacheCitiesUseCase(forceRefresh)
                .onSuccess {
                    onRefreshFinished.emit(Unit)
                }
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
            onRefreshFinished,
        ) { searchBarViewState, cities, selectedCity, _ ->
            val filteredCities = filterCitiesUseCase(
                cities = cities,
                prefix = searchBarViewState.searchQuery,
                onlyFavorites = searchBarViewState.onlyFavorites
            )
            filteredCities to selectedCity
        }
        .onEach { (result, selectedCity) ->
            val cities = result.map { city ->
                city.toCityItemViewState(city.id == selectedCity)
            }
            _viewState.update {
                when {
                    cities.isEmpty() &&
                        it is CityListViewState.Loading.Network -> CityListViewState.Loading.Local
                    cities.isEmpty() -> CityListViewState.Empty
                    else -> CityListViewState.Success(cities)
                }
            }
        }
        .catch { e ->
            e.printStackTrace()
            _viewState.value = CityListViewState.Error.General
        }.launchIn(viewModelScope + ioDispatcher)
    }

    fun onEvent(event: CityListUiEvent) {
        when(event) {
            is CityListUiEvent.OnSearchQueryChange -> {
                _searchBarViewState.update { it.copy(searchQuery = event.query) }
            }
            is CityListUiEvent.OnCityClick -> onCitySelected(event)
            is CityListUiEvent.OnCityFavoriteClick -> onCityFavoriteClick(event)
            is CityListUiEvent.OnRefresh -> fetchCitiesFromApiIfNeeded(true)
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

    private fun onCityFavoriteClick(event: CityListUiEvent.OnCityFavoriteClick) {
        setCityFavoriteState(event.cityId, FavoriteState.Loading)
        viewModelScope.launch(ioDispatcher) {
            val stateToUpdate = !event.favoriteState.toBoolean()
            val updateSuccessful = updateCityFavoriteStatusUseCase(
                cityId = event.cityId,
                isFavorite = stateToUpdate,
            )
            if (!updateSuccessful) {
                setCityFavoriteState(
                    cityId = event.cityId,
                    favoriteState = event.favoriteState,
                )
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

    private fun setCityFavoriteState(cityId: Long, favoriteState: FavoriteState) {
        _viewState.update {
            when (val state = it) {
                is CityListViewState.Success -> state.copy(
                    cities = state.cities.map { city ->
                        if (city.id == cityId) {
                            city.copy(favoriteState = favoriteState)
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