package com.juan.di.modules

import android.app.Application
import androidx.room.Room
import com.juan.db.CityDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(
        app: Application
    ): CityDatabase {
        return Room.databaseBuilder(
            app,
            CityDatabase::class.java,
            "cities_database"
        ).build()
    }

    @Provides
    fun provideCityDao(db: CityDatabase) = db.cityDao()

    @Provides
    fun provideFavoriteCityDao(db: CityDatabase) = db.favoriteCityDao()
}