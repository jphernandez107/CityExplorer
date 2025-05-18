package com.juan.ui.shared

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class SelectionModule {

    @Binds
    @Singleton
    abstract fun bindCitySelectionManager(
        citySelectionManagerImpl: CitySelectionManagerImpl
    ): CitySelectionManager
}