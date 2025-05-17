package com.juan.ui.details.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberUpdatedMarkerState
import com.juan.ui.citylist.FavoriteState
import com.juan.ui.components.FavoriteIcon
import com.juan.ui.details.CityDetailsViewModel
import com.juan.ui.details.CityDetailsViewState
import com.juan.ui.map.CityDetailsUiEvent

@Composable
internal fun CityDetailsScreen(
    cityDetailsViewModel: CityDetailsViewModel = hiltViewModel()
) {
    val viewState by cityDetailsViewModel.viewState.collectAsState()
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
            .padding(16.dp),
    ) {
        Text(
            text = "Coordinates",
            style = MaterialTheme.typography.titleMedium,
        )
        Text(
            text = viewState.coordinates,
            style = MaterialTheme.typography.bodyMedium,
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(text = "Favorite: ")
            IconButton(
                onClick = onFavoriteClick,
                enabled = viewState.favoriteState != FavoriteState.Loading,
            ) {
                FavoriteIcon(viewState.favoriteState)
            }
        }

        Spacer(Modifier.height(8.dp))

        Text(
            text = "Location",
            style = MaterialTheme.typography.titleMedium,
        )
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