package com.juan.ui.map.screen

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberUpdatedMarkerState
import com.juan.domain.model.City
import com.juan.ui.map.CityMapViewModel
import com.juan.ui.map.CityMapViewState

@Composable
internal fun CityMapScreen(
    modifier: Modifier = Modifier,
    cityMapViewModel: CityMapViewModel = hiltViewModel(),
) {
    val viewState by cityMapViewModel.viewState.collectAsState()
    when (viewState) {
        is CityMapViewState.Loading -> Unit
        is CityMapViewState.Error -> Unit
        is CityMapViewState.Success -> {
            val city = (viewState as CityMapViewState.Success).city
            MapContent(
                city = city,
                modifier = modifier
            )
        }
    }
}

@Composable
private fun MapContent(
    city: City,
    modifier: Modifier
) {
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(
            LatLng(city.geoCoordinates.latitude, city.geoCoordinates.longitude),
            11f,
        )
    }
    val markerState = rememberUpdatedMarkerState(
        position = LatLng(
            city.geoCoordinates.latitude,
            city.geoCoordinates.longitude,
        ),
    )
    GoogleMap(
        modifier = modifier
            .fillMaxSize(),
        cameraPositionState = cameraPositionState,
    ) {
        Marker(
            state = markerState,
            title = city.name,
            snippet = city.country,
        )
    }
}