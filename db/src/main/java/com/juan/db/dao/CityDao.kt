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

    @Query("SELECT EXISTS(SELECT 1 FROM cities LIMIT 1)")
    suspend fun hasCities(): Boolean

    @Query("SELECT * FROM cities WHERE id = :id LIMIT 1")
    fun getCityById(id: Long): Flow<CityEntity?>
}