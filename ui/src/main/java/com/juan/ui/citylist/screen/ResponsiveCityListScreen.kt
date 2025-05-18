package com.juan.ui.citylist.screen

import android.content.res.Configuration
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.juan.ui.citylist.CityListViewModel
import com.juan.ui.map.CityMapViewModel
import com.juan.ui.map.screen.CityMapScreen

@Composable
internal fun ResponsiveCityListScreen(
    cityListViewModel: CityListViewModel,
    cityMapViewModel: CityMapViewModel,
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
) {
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
    val lazyListState = rememberLazyListState()

    if (isLandscape) {
        LandscapeCityListScreen(
            cityListViewModel = cityListViewModel,
            cityMapViewModel = cityMapViewModel,
            lazyListState = lazyListState,
            modifier = modifier,
            navController = navController,
        )
    } else {
        CityListScreen(
            modifier = modifier.fillMaxSize(),
            viewModel = cityListViewModel,
            lazyListState = lazyListState,
            navController = navController,
        )
    }
}

@Composable
private fun LandscapeCityListScreen(
    cityListViewModel: CityListViewModel,
    cityMapViewModel: CityMapViewModel,
    lazyListState: LazyListState,
    modifier: Modifier = Modifier,
    navController: NavHostController,
) {
    Row(
        modifier = modifier
            .fillMaxSize()
    ) {
        CityListScreen(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(0.45f),
            viewModel = cityListViewModel,
            lazyListState = lazyListState,
            navController = navController,
        )
        CityMapScreen(
            modifier = Modifier
                .fillMaxHeight()
                .padding(top = 8.dp, bottom = 8.dp, end = 8.dp)
                .clip(RoundedCornerShape(12.dp)),
            cityMapViewModel = cityMapViewModel,
        )
    }
}