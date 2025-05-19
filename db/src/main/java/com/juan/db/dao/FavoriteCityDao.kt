package com.juan.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.juan.db.entity.FavoriteCityEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteCityDao {

    @Query("SELECT * FROM favorite_cities")
    fun getAllFavorites(): Flow<List<FavoriteCityEntity>>

    @Query("SELECT * FROM favorite_cities WHERE cityId = :cityId LIMIT 1")
    fun getFavoriteByCityId(cityId: Long): Flow<FavoriteCityEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavoriteCity(favorite: FavoriteCityEntity)
}