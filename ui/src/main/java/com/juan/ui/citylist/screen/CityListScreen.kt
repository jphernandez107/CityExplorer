package com.juan.ui.citylist.screen

import android.content.res.Configuration
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.flowWithLifecycle
import androidx.navigation.NavController
import com.juan.ui.citylist.CityItemViewState
import com.juan.ui.citylist.CityListUiEvent
import com.juan.ui.citylist.CityListViewModel
import com.juan.ui.citylist.CityListViewState
import com.juan.ui.citylist.CityNavigationEvent
import com.juan.ui.citylist.FavoriteState
import com.juan.ui.citylist.SearchBarViewState
import com.juan.ui.components.CitySearchBar
import com.juan.ui.components.FavoriteIcon
import com.juan.ui.navigation.Screen
import kotlinx.coroutines.flow.collectLatest

@Composable
fun CityListScreen(
    navController: NavController,
    lazyListState: LazyListState,
    modifier: Modifier = Modifier,
    viewModel: CityListViewModel = hiltViewModel(),
) {
    val viewState by viewModel.viewState.collectAsState()
    val searchBarViewState by viewModel.searchBarViewState.collectAsState()
    CityListScreenContent(
        modifier = modifier,
        viewState = viewState,
        searchBarViewState = searchBarViewState,
        lazyListState = lazyListState,
        onEvent = viewModel::onEvent,
    )

    val lifecycleOwner = LocalLifecycleOwner.current
    val isPortrait = with(LocalConfiguration.current) {
        orientation == Configuration.ORIENTATION_PORTRAIT
    }
    LaunchedEffect(Unit) {
        viewModel.navigationEvent
            .flowWithLifecycle(lifecycleOwner.lifecycle)
            .collectLatest { event ->
                when (event) {
                    is CityNavigationEvent.NavigateToMapIfPortrait -> if (isPortrait) {
                        navController.navigate(Screen.CityMap.createRoute(event.cityId))
                    }
                    is CityNavigationEvent.NavigateToCityDetails ->
                        navController.navigate(Screen.CityDetails.createRoute(event.cityId))
                }
            }
    }
}


@Composable
private fun CityListScreenContent(
    modifier: Modifier = Modifier,
    viewState: CityListViewState,
    searchBarViewState: SearchBarViewState,
    lazyListState: LazyListState = rememberLazyListState(),
    onEvent: (CityListUiEvent) -> Unit
) {
    Column(
        modifier = modifier,
    ) {
        CitySearchBar(
            searchBarViewState = searchBarViewState,
            onQueryChanged = { onEvent(CityListUiEvent.OnSearchQueryChange(it)) },
            onFavoriteToggle = { onEvent(CityListUiEvent.OnOnlyFavoritesClick) },
            onClearClick = { onEvent(CityListUiEvent.OnSearchQueryChange("")) }
        )

        when (viewState) {
            is CityListViewState.Loading -> {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
            is CityListViewState.Error -> {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Something went wrong. Try refreshing.")
                }
            }
            is CityListViewState.Empty -> {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("No cities match your search.")
                }
            }
            is CityListViewState.Success -> {
                LazyColumn(
                    state = lazyListState,
                ) {
                    items(viewState.cities, key = { it.id }) { city ->
                        CityItemCard(
                            city = city,
                            onRowClick = { onEvent(CityListUiEvent.OnCityClick(city.id)) },
                            onDetailsClick = { onEvent(CityListUiEvent.OnCityDetailsClick(city.id)) },
                            onFavoriteToggle = {
                                onEvent(CityListUiEvent.OnCityFavoriteClick(city.id, city.favoriteState))
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun CityItemCard(
    city: CityItemViewState,
    onRowClick: () -> Unit,
    onDetailsClick: () -> Unit = {},
    onFavoriteToggle: () -> Unit,
) {
    val borderColor = if (city.isSelected) {
        MaterialTheme.colorScheme.outline
    } else {
        Color.Transparent
    }
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .border(2.dp, borderColor, RoundedCornerShape(12.dp))
            .clickable { onRowClick() },
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(8.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 4.dp, top = 8.dp, bottom = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Column {
                Text(
                    text = "${city.name}, ${city.country}",
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = city.coordinates,
                    style = MaterialTheme.typography.labelSmall
                )
            }
            Row {
                IconButton(
                    onClick = onDetailsClick,
                    enabled = city.favoriteState !is FavoriteState.Loading,
                ) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = "View Details"
                    )
                }
                IconButton(
                    onClick = onFavoriteToggle,
                    enabled = city.favoriteState !is FavoriteState.Loading,
                ) {
                    FavoriteIcon(city.favoriteState)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun CityListScreenPreview() = MaterialTheme {
    CityListScreenContent(
        viewState = CityListViewState.Success(
            cities = listOf(
                CityItemViewState(
                    id = 1,
                    name = "New York",
                    country = "USA",
                    favoriteState = FavoriteState.Favorite,
                    coordinates = "40.7128° N, 74.0060° W",
                ),
                CityItemViewState(
                    id = 2,
                    name = "Los Angeles",
                    country = "USA",
                    favoriteState = FavoriteState.NotFavorite,
                    coordinates = "34.0522° N, 118.2437° W",
                    isSelected = true,
                ),
                CityItemViewState(
                    id = 3,
                    name = "Miami",
                    country = "USA",
                    favoriteState = FavoriteState.Loading,
                    coordinates = "32.0522° N, 92.2437° W",
                ),
            ),
        ),
        searchBarViewState = SearchBarViewState(
            searchQuery = "",
            onlyFavorites = false,
        ),
        onEvent = {},
    )
}