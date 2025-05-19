package com.juan.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.juan.db.dao.CityDao
import com.juan.db.dao.FavoriteCityDao
import com.juan.db.entity.CityEntity
import com.juan.db.entity.FavoriteCityEntity

@Database(
    entities = [
        CityEntity::class,
        FavoriteCityEntity::class,
    ],
    version = 1,
)
abstract class CityDatabase : RoomDatabase() {
    abstract fun cityDao(): CityDao
    abstract fun favoriteCityDao(): FavoriteCityDao
}