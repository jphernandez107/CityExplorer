package com.juan.ui.citylist

import androidx.compose.runtime.Immutable

@Immutable
data class SearchBarViewState(
    val searchQuery: String,
    val onlyFavorites: Boolean = false,
)
