package com.juan.ui.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.juan.ui.citylist.CityListViewModel
import com.juan.ui.citylist.screen.ResponsiveCityListScreen
import com.juan.ui.details.screen.CityDetailsScreen
import com.juan.ui.map.CityMapViewModel
import com.juan.ui.map.screen.CityMapScreen

@Composable
fun AppNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
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
                CityDetailsScreen(
                    navController = navController,
                )
            }
        }
    }
}
