package com.juan.ui.navigation

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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.juan.ui.citylist.screen.CityListScreen
import com.juan.ui.details.CityDetailsViewModel
import com.juan.ui.details.screen.CityDetailsScreen
import com.juan.ui.map.screen.CityMapScreen

@Composable
fun AppNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = currentBackStackEntry?.destination

    val showBackButton = currentDestination?.route != Screen.CityList.route
    val showTopBar = currentDestination?.route != Screen.CityMap.route
    var cityDetailsTitle by remember { mutableStateOf("City Details") }
    val title = when (currentDestination?.route) {
        Screen.CityList.route -> "City List"
        Screen.CityMap.route -> "City Map"
        Screen.CityDetails.route -> cityDetailsTitle
        else -> "City Explorer"
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            if (showTopBar) {
                CityExplorerTopBar(
                    title = title,
                    showBackButton = showBackButton,
                    onBackClick = { navController.popBackStack() },
                )
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.CityList.route,
            modifier = modifier.padding(innerPadding),
        ) {
            composable(
                route = Screen.CityList.route,
            ) {
                CityListScreen(
                    navController = navController,
                )
            }
            composable(
                route = Screen.CityMap.route
            ) {
                CityMapScreen()
            }
            composable(
                route = Screen.CityDetails.route,
            ) {
                val cityDetailsViewModel = hiltViewModel<CityDetailsViewModel>()
                val cityTitle by cityDetailsViewModel.screenTitle.collectAsState()
                CityDetailsScreen(
                    cityDetailsViewModel = cityDetailsViewModel,
                )
                LaunchedEffect(cityTitle) {
                    cityDetailsTitle = cityTitle
                }
            }
        }
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun CityExplorerTopBar(
    title: String,
    showBackButton: Boolean,
    onBackClick: () -> Unit,
) {
    TopAppBar(
        title = {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
            )
        },
        navigationIcon = {
            if (showBackButton) {
                IconButton(
                    onClick = onBackClick,
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back Button",
                    )
                }
            }
        }
    )
}