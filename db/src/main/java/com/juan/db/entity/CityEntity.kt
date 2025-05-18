package com.juan.db.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "cities",
    indices = [
        Index(value = ["name"]),
        Index(value = ["isFavorite"]),
    ],
)
data class CityEntity(
    @PrimaryKey val id: Long,
    val name: String,
    val country: String,
    val latitude: Double,
    val longitude: Double,
    val coordinatesString: String,
    val isFavorite: Boolean = false,
)
