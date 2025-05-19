package com.juan.ui.details.screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Place
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberUpdatedMarkerState
import com.juan.ui.citylist.FavoriteState
import com.juan.ui.components.FavoriteIcon
import com.juan.ui.components.TopBar
import com.juan.ui.details.CityDetailsViewModel
import com.juan.ui.details.CityDetailsViewState
import com.juan.ui.map.CityDetailsUiEvent

@Composable
internal fun CityDetailsScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    cityDetailsViewModel: CityDetailsViewModel = hiltViewModel(),
) {
    val viewState by cityDetailsViewModel.viewState.collectAsState()
    Column(
        modifier = modifier
            .fillMaxSize(),
    ) {
        TopBar(
            title = "City Details",
            onBackClick = {
                navController.popBackStack()
            }
        )
        when (val state = viewState) {
            is CityDetailsViewState.Loading -> LoadingCityDetailsScreen()
            is CityDetailsViewState.Error -> ErrorCityDetailsScreen()
            is CityDetailsViewState.Success -> SuccessCityDetailsScreen(
                viewState = state,
                onFavoriteClick = {
                    cityDetailsViewModel.onEvent(CityDetailsUiEvent.OnFavoriteClick(state.favoriteState))
                }
            )
        }
    }
}

@Composable
private  fun LoadingCityDetailsScreen(
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier.fillMaxSize(),
    ) {
        CircularProgressIndicator()
    }
}

@Composable
private fun ErrorCityDetailsScreen(
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier.fillMaxSize(),
    ) {
        Text(
            text = "Error loading city details",
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}

@Composable
private fun SuccessCityDetailsScreen(
    viewState: CityDetailsViewState.Success,
    onFavoriteClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(
            LatLng(viewState.latitude, viewState.longitude),
            12f,
        )
    }
    val markerState = rememberUpdatedMarkerState(
        position = LatLng(viewState.latitude, viewState.longitude)
    )
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(
                state = rememberScrollState()
            ),
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
        ) { 
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 24.dp),
            ) {
                Text(
                    text = viewState.name,
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                    ),
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = viewState.country,
                    style = MaterialTheme.typography.titleMedium.copy(),
                    color = MaterialTheme.colorScheme.secondary,
                )
                Spacer(Modifier.height(16.dp))
                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Place,
                        contentDescription = "Location"
                    )
                    Text(
                        text = viewState.coordinates,
                        style = MaterialTheme.typography.bodyLarge,
                    )
                }
                Spacer(Modifier.height(16.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    val toggleFavoriteText = when (viewState.favoriteState) {
                        FavoriteState.Favorite -> "Remove from favorites"
                        FavoriteState.NotFavorite -> "Add to favorites"
                        FavoriteState.Loading -> "Loading..."
                    }
                    FavoriteIcon(viewState.favoriteState)
                    Button(
                        onClick = onFavoriteClick,
                        enabled = viewState.favoriteState !is FavoriteState.Loading,
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.elevatedButtonColors(),
                        border = BorderStroke(2.dp, MaterialTheme.colorScheme.primary),
                    ) {
                        Text(
                            text = toggleFavoriteText,
                            style = MaterialTheme.typography.titleSmall,
                        )
                    }
                }
            }
        }

        Spacer(Modifier.height(24.dp))

        Text(
            text = "Location",
            style = MaterialTheme.typography.titleMedium,
        )
        Spacer(Modifier.height(8.dp))
        GoogleMap(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .clip(RoundedCornerShape(16.dp)),
            cameraPositionState = cameraPositionState,
        ) {
            Marker(
                state = markerState,
                title = "${viewState.name}, ${viewState.country}",
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun CityDetailsScreenPreview() = MaterialTheme {
    SuccessCityDetailsScreen(
        viewState = CityDetailsViewState.Success(
            cityId = 1,
            name = "New York",
            country = "US",
            latitude = 12.34,
            longitude = 56.78,
            coordinates = "12.34, 56.78",
            favoriteState = FavoriteState.Favorite,
        ),
        onFavoriteClick = {},
    )
}