package com.juan.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.juan.db.entity.CityEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CityDao {
    @Query("SELECT * FROM cities ORDER BY name ASC")
    fun getAllCities(): Flow<List<CityEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCities(cities: List<CityEntity>)

    @Query("UPDATE cities SET isFavorite = :isFavorite WHERE id = :cityId")
    suspend fun updateCityFavoriteStatus(cityId: Long, isFavorite: Boolean): Int

    @Query("SELECT EXISTS(SELECT 1 FROM cities LIMIT 1)")
    suspend fun hasCities(): Boolean
}