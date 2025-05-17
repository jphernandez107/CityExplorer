package com.juan.ui.navigation

sealed class Screen(val route: String) {
    data object CityList : Screen("city_list")
    data object CityMap : Screen("city_map/{cityId}") {
        fun createRoute(cityId: Long) = "city_map/$cityId"
    }
    data object CityDetails : Screen("city_details/{cityId}") {
        fun createRoute(cityId: Long) = "city_details/$cityId"
    }

    companion object {
        const val CITY_ID_KEY = "cityId"
    }
}