package com.juan.ui.shared

import kotlinx.coroutines.flow.StateFlow

/**
 * Manages the selected city in memory for UI coordination between screens.
 */
interface CitySelectionManager {

    /**
     * A [StateFlow] that holds the currently selected city ID.
     * Nullable when no city is selected.
     */
    val selectedCityId: StateFlow<Long?>

    /**
     * Updates the selected city ID.
     *
     * @param city the ID of the selected city, or null to clear selection.
     */
    fun selectCityId(city: Long?)
}