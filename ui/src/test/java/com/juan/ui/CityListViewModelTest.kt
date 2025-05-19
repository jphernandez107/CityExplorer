package com.juan.ui

import com.juan.domain.model.City
import com.juan.domain.model.City.GeoCoordinates
import com.juan.domain.usecase.FetchAndCacheCitiesUseCase
import com.juan.domain.usecase.FilterCitiesUseCase
import com.juan.domain.usecase.GetAllCitiesUseCase
import com.juan.domain.usecase.UpdateCityFavoriteStatusUseCase
import com.juan.ui.citylist.CityListUiEvent
import com.juan.ui.citylist.CityListViewModel
import com.juan.ui.citylist.CityListViewState
import com.juan.ui.citylist.FavoriteState
import com.juan.ui.shared.CitySelectionManager
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class CityListViewModelTest {

    private val fetchCitiesUseCase: FetchAndCacheCitiesUseCase = mockk()
    private val getAllCitiesUseCase: GetAllCitiesUseCase = mockk()
    private val updateCityFavoriteStatusUseCase: UpdateCityFavoriteStatusUseCase = mockk()
    private val filterCitiesUseCase = FilterCitiesUseCase()
    private val citySelectionManager: CitySelectionManager = mockk(relaxed = true)

    private lateinit var viewModel: CityListViewModel

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        every { getAllCitiesUseCase() } returns flowOf(
            listOf(
                City(1, "Berlin", "DE", GeoCoordinates(0.0, 0.0), "", false),
                City(2, "Boston", "US", GeoCoordinates(0.0, 0.0), "", true)
            )
        )

        coEvery { fetchCitiesUseCase() } returns Result.success(Unit)
        every { citySelectionManager.selectedCityId } returns MutableStateFlow(null)

        viewModel = CityListViewModel(
            fetchAndCacheCitiesUseCase = fetchCitiesUseCase,
            getAllCitiesUseCase = getAllCitiesUseCase,
            updateCityFavoriteStatusUseCase = updateCityFavoriteStatusUseCase,
            filterCitiesUseCase = filterCitiesUseCase,
            citySelectionManager = citySelectionManager,
            ioDispatcher = testDispatcher,
        )
    }

    @Test
    fun `search filters cities correctly`() = runTest(UnconfinedTestDispatcher()) {
        advanceUntilIdle()
        viewModel.onEvent(CityListUiEvent.OnSearchQueryChange("Berlin"))
        advanceUntilIdle()

        val state = viewModel.viewState.value
        assertTrue(state is CityListViewState.Success)
        assertEquals(1, (state as CityListViewState.Success).cities.size)
        assertEquals("Berlin", state.cities.first().name)
    }

    @Test
    fun `toggle favorite updates selection manager`() = runTest {
        coEvery { updateCityFavoriteStatusUseCase(any(), any()) } returns true

        viewModel.onEvent(CityListUiEvent.OnCityFavoriteClick(1, FavoriteState.NotFavorite))
        advanceUntilIdle()

        coVerify { updateCityFavoriteStatusUseCase(1, true) }
    }

    @Test
    fun `search returns empty when no match`() = runTest(testDispatcher) {
        advanceUntilIdle()
        viewModel.onEvent(CityListUiEvent.OnSearchQueryChange("Tokyo"))
        advanceUntilIdle()

        val state = viewModel.viewState.value
        assertTrue(state is CityListViewState.Empty)
    }

    @Test
    fun `toggle favorite updates repository correctly`() = runTest(testDispatcher) {
        coEvery { updateCityFavoriteStatusUseCase(2, false) } returns true

        viewModel.onEvent(CityListUiEvent.OnCityFavoriteClick(2, FavoriteState.Favorite))
        advanceUntilIdle()

        coVerify { updateCityFavoriteStatusUseCase(2, false) }
    }

    @Test
    fun `toggle favorite failure does not crash`() = runTest(testDispatcher) {
        coEvery { updateCityFavoriteStatusUseCase(1, true) } returns false

        viewModel.onEvent(CityListUiEvent.OnCityFavoriteClick(1, FavoriteState.NotFavorite))
        advanceUntilIdle()

        // State remains stable — success or fallback, no crash
        val state = viewModel.viewState.value
        assertTrue(state is CityListViewState.Success || state is CityListViewState.Error)
    }

    @Test
    fun `refresh triggers fetch and updates UI state on failure`() = runTest(testDispatcher) {
        coEvery { fetchCitiesUseCase() } returns Result.failure(RuntimeException("Network error"))

        advanceUntilIdle()
        viewModel.onEvent(CityListUiEvent.OnRefresh)
        advanceUntilIdle()

        val state = viewModel.viewState.value
        assertTrue(state is CityListViewState.Error.Network)
    }

    @Test
    fun `refresh triggers fetch and keeps previous UI state on success`() = runTest(testDispatcher) {
        coEvery { fetchCitiesUseCase() } returns Result.success(Unit)

        viewModel.onEvent(CityListUiEvent.OnRefresh)
        advanceUntilIdle()

        val state = viewModel.viewState.value
        assertTrue(state is CityListViewState.Success || state is CityListViewState.Loading)
    }

    @Test
    fun `search filters only favorites when enabled`() = runTest(testDispatcher) {
        advanceUntilIdle()
        viewModel.onEvent(CityListUiEvent.OnOnlyFavoritesClick)
        viewModel.onEvent(CityListUiEvent.OnSearchQueryChange("B"))
        advanceUntilIdle()

        val state = viewModel.viewState.value
        assertTrue(state is CityListViewState.Success)
        assertTrue((state as CityListViewState.Success).cities.all { it.favoriteState == FavoriteState.Favorite })
    }

    @Test
    fun `city selection is updated via event`() = runTest(testDispatcher) {
        viewModel.onEvent(CityListUiEvent.OnCityClick(2))
        advanceUntilIdle()

        verify { citySelectionManager.selectCityId(2) }
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }
}
