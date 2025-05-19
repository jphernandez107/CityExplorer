package com.juan.db.mapper

import com.juan.db.entity.CityEntity
import com.juan.domain.model.City

fun CityEntity.toDomain(isFavorite: Boolean) = City(
    id = id,
    name = name,
    country = country,
    geoCoordinates = City.GeoCoordinates(
        latitude = latitude,
        longitude = longitude
    ),
    coordinatesString = coordinatesString,
    isFavorite = isFavorite,
)