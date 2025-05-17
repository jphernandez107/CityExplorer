package com.juan.domain.model

data class City(
    val id: Long,
    val name: String,
    val country: String,
    val geoCoordinates: GeoCoordinates,
    val coordinatesString: String,
    val isFavorite: Boolean = false,
) {
    data class GeoCoordinates(
        val latitude: Double,
        val longitude: Double,
    )
}
