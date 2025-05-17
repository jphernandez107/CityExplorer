package com.juan.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.juan.db.dao.CityDao
import com.juan.db.entity.CityEntity

@Database(
    entities = [
        CityEntity::class,
    ],
    version = 1,
)
abstract class CityDatabase : RoomDatabase() {
    abstract fun cityDao(): CityDao
}