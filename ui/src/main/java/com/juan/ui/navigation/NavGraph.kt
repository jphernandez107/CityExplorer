package com.juan.ui.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.juan.ui.citylist.CityListViewModel
import com.juan.ui.citylist.screen.ResponsiveCityListScreen
import com.juan.ui.details.CityDetailsViewModel
import com.juan.ui.details.screen.CityDetailsScreen
import com.juan.ui.map.CityMapViewModel
import com.juan.ui.map.screen.CityMapScreen

@Composable
fun AppNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = currentBackStackEntry?.destination

    val showBackButton = currentDestination?.route != Screen.CityList.route
    val showTopBar = currentDestination?.route == Screen.CityDetails.route
    var cityDetailsTitle by remember { mutableStateOf("City Details") }
    val title = when (currentDestination?.route) {
        Screen.CityList.route -> "City List"
        Screen.CityMap.route -> "City Map"
        Screen.CityDetails.route -> cityDetailsTitle
        else -> "City Explorer"
    }

    val cityListViewModel = hiltViewModel<CityListViewModel>()
    val cityMapViewModel = hiltViewModel<CityMapViewModel>()

    Scaffold(
        modifier = modifier.fillMaxSize()
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.CityList.route,
            modifier = Modifier.padding(innerPadding),
        ) {
            composable(
                route = Screen.CityList.route,
            ) {
                ResponsiveCityListScreen(
                    cityListViewModel = cityListViewModel,
                    cityMapViewModel = cityMapViewModel,
                    navController = navController,
                )
            }
            composable(
                route = Screen.CityMap.route
            ) {
                CityMapScreen(
                    navController = navController,
                    cityMapViewModel = cityMapViewModel,
                )
            }
            composable(
                route = Screen.CityDetails.route,
            ) {
                val cityDetailsViewModel = hiltViewModel<CityDetailsViewModel>()
                val cityTitle by cityDetailsViewModel.screenTitle.collectAsState()
                CityDetailsScreen(
                    navController = navController,
                    cityDetailsViewModel = cityDetailsViewModel,
                )
                LaunchedEffect(cityTitle) {
                    cityDetailsTitle = cityTitle
                }
            }
        }
    }
}
