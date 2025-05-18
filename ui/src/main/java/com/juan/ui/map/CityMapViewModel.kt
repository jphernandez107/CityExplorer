package com.juan.ui.map

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.juan.domain.model.City
import com.juan.domain.usecase.GetCityByIdUseCase
import com.juan.ui.shared.CitySelectionManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CityMapViewModel  @Inject constructor(
    private val getCityByIdUseCase: GetCityByIdUseCase,
    private val citySelectionManager: CitySelectionManager,
) : ViewModel() {
    private val _viewState = MutableStateFlow<CityMapViewState>(CityMapViewState.NoSelection)
    val viewState = _viewState.asStateFlow()

    init {
        observeCitySelection()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun observeCitySelection() {
        viewModelScope.launch {
            citySelectionManager.selectedCityId
                .filterNotNull()
                .distinctUntilChanged()
                .flatMapLatest { cityId ->
                    getCityByIdUseCase(cityId)
                }
                .collectLatest(::updateMapViewState)
        }
    }

    private fun updateMapViewState(city: City?) {
        _viewState.update {
            city?.let { CityMapViewState.Success(it) } ?: CityMapViewState.Error
        }
    }
}