package com.juan.data.mapper

import com.juan.data.model.CityDto
import com.juan.db.entity.CityEntity

fun CityDto.toEntity() = CityEntity(
    id = id,
    name = name,
    country = country,
    latitude = geoCoordinates.latitude,
    longitude = geoCoordinates.longitude,
    coordinatesString = geoCoordinates.toString(),
)