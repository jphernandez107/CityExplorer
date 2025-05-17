package com.juan.data.model

import com.google.gson.annotations.SerializedName
import java.util.Locale
import kotlin.math.abs

data class CityDto(
    @SerializedName("_id") val id: Long,
    @SerializedName("name") val name: String,
    @SerializedName("country") val country: String,
    @SerializedName("coord") val geoCoordinates: GeoCoordinatesDto,
)

class GeoCoordinatesDto(
    @SerializedName("lat") val latitude: Double,
    @SerializedName("lon") val longitude: Double,
) {
    /**
     * Format the coordinates into a string like: "40.7128° N, 74.0060° W"
     */
    override fun toString(): String {
        val latitudeDirection = if (latitude >= 0) "N" else "S"
        val longitudeDirection = if (longitude >= 0) "E" else "O"
        val formattedLatitude = String.format(Locale.getDefault(), "%.4f", abs(latitude))
        val formattedLongitude = String.format(Locale.getDefault(), "%.4f", abs(longitude))
        return "${formattedLatitude}° $latitudeDirection, ${formattedLongitude}° $longitudeDirection"
    }
}