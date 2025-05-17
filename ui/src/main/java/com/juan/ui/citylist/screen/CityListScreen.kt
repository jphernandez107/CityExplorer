package com.juan.ui.citylist.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.juan.ui.citylist.CityItemViewState
import com.juan.ui.citylist.CityListUiEvent
import com.juan.ui.citylist.CityListViewModel
import com.juan.ui.citylist.CityListViewState
import com.juan.ui.citylist.SearchBarViewState
import com.juan.ui.components.CitySearchBar

@Composable
fun CityListScreen(
    viewModel: CityListViewModel = hiltViewModel(),
) {
    val viewState by viewModel.viewState.collectAsState()
    val searchBarViewState by viewModel.searchBarViewState.collectAsState()
    CityListScreenContent(
        viewState = viewState,
        searchBarViewState = searchBarViewState,
        onEvent = viewModel::onEvent,
    )
}


@Composable
private fun CityListScreenContent(
    viewState: CityListViewState,
    searchBarViewState: SearchBarViewState,
    onEvent: (CityListUiEvent) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
    ) {
        CitySearchBar(
            searchBarViewState = searchBarViewState,
            onQueryChanged = { onEvent(CityListUiEvent.OnSearchQueryChange(it)) },
            onFavoriteToggle = { onEvent(CityListUiEvent.OnOnlyFavoritesClick) }
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
                LazyColumn {
                    items(viewState.cities, key = { it.id }) { city ->
                        CityItemCard(
                            city = city,
                            onClick = { onEvent(CityListUiEvent.OnCityClick(city.id)) },
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
    onClick: () -> Unit,
    onFavoriteToggle: () -> Unit,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable { onClick() },
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
            IconButton(
                onClick = onFavoriteToggle,
                enabled = city.favoriteState !is CityItemViewState.FavoriteState.Loading,
            ) {
                FavoriteIcon(city.favoriteState)
            }
        }
    }
}

@Composable
private fun FavoriteIcon(favoriteState: CityItemViewState.FavoriteState) {
    when (favoriteState) {
        is CityItemViewState.FavoriteState.Loading ->
            CircularProgressIndicator(
                modifier = Modifier.size(24.dp),
                strokeWidth = 2.dp,
            )
        is CityItemViewState.FavoriteState.NotFavorite ->
            Icon(
                imageVector = Icons.Outlined.FavoriteBorder,
                contentDescription = "Toggle Favorite"
            )
        is CityItemViewState.FavoriteState.Favorite ->
            Icon(
                imageVector = Icons.Outlined.Favorite,
                contentDescription = "Toggle Favorite"
            )
    }
}

@Preview
@Composable
private fun CityListScreenPreview() = MaterialTheme {
    CityListScreenContent(
        viewState = CityListViewState.Success(
            cities = listOf(
                CityItemViewState(
                    id = 1,
                    name = "New York",
                    country = "USA",
                    favoriteState = CityItemViewState.FavoriteState.Favorite,
                    coordinates = "40.7128° N, 74.0060° W",
                ),
                CityItemViewState(
                    id = 2,
                    name = "Los Angeles",
                    country = "USA",
                    favoriteState = CityItemViewState.FavoriteState.NotFavorite,
                    coordinates = "34.0522° N, 118.2437° W",
                ),
                CityItemViewState(
                    id = 2,
                    name = "Miami",
                    country = "USA",
                    favoriteState = CityItemViewState.FavoriteState.Loading,
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