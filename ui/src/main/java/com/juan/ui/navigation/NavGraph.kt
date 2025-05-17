package com.juan.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.juan.ui.citylist.screen.CityListScreen

@Composable
fun AppNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = navController,
        startDestination = Screen.CityList.route,
        modifier = modifier,
    ) {
        composable(
            route = Screen.CityList.route,
        ) {
            CityListScreen()
        }
        composable(
            route = Screen.CityMap.route
        ) {

        }
        composable(
            route = Screen.CityDetail.route,
        ) {
//            CityDetailsScreen(
//                onBackClick = {
//                    navController.popBackStack()
//                }
//            )
        }
    }
}