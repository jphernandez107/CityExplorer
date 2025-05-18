package com.juan.ui.map.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.maps.CameraUpdateFactory
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
    showCloseButton: Boolean = true,
    navController: NavController = rememberNavController(),
    cityMapViewModel: CityMapViewModel = hiltViewModel(),
) {
    val viewState by cityMapViewModel.viewState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    val city = (viewState as? CityMapViewState.Success)?.city

    Box (
        modifier = modifier
            .fillMaxSize(),
    ) {
        MapContent(
            city = city,
        )
        if (showCloseButton) {
            IconButton(
                onClick = { navController.popBackStack() },
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(16.dp)
                    .background(
                        color = MaterialTheme.colorScheme.background,
                        shape = CircleShape,
                    )
                    .clip(CircleShape),
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Close",
                )
            }
        }

        if (viewState is CityMapViewState.Error) {
            LaunchedEffect(Unit) {
                snackbarHostState.showSnackbar(message = "City not found")
            }
        }

        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(16.dp),
        )
    }
}

@Composable
private fun MapContent(
    city: City?,
    modifier: Modifier = Modifier,
) {
    val cameraPositionState = rememberCameraPositionState()
    val markerState = rememberUpdatedMarkerState()

    LaunchedEffect(city?.id) {
        city?.let {
            val target = LatLng(
                it.geoCoordinates.latitude,
                it.geoCoordinates.longitude,
            )
            markerState.position = target
            with(cameraPositionState) {
                animate(
                    update = CameraUpdateFactory.newLatLngZoom(target, 6f),
                    durationMs = 400,
                )
                animate(
                    update = CameraUpdateFactory.zoomTo(11f),
                    durationMs = 400,
                )
            }
        }
    }

    GoogleMap(
        modifier = modifier
            .fillMaxSize(),
        cameraPositionState = cameraPositionState,
    ) {
        city?.let {
            Marker(
                state = markerState,
                title = it.name,
                snippet = it.country,
            )
        }
    }
}