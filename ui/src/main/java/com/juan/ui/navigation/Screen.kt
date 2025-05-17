package com.juan.ui.navigation

sealed class Screen(val route: String) {
    data object CityList : Screen("city_list")
    data object CityMap : Screen("city_map/{cityId}") {
        fun createRoute(cityId: Int) = "city_map/$cityId"
    }
    data object CityDetail : Screen("city_detail/{cityId}") {
        fun createRoute(cityId: Int) = "city_detail/$cityId"
    }
}