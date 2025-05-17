package com.juan.ui.citylist

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable

@Stable
sealed interface FavoriteState {
    @Immutable
    data object Favorite : FavoriteState

    @Immutable
    data object NotFavorite : FavoriteState

    @Immutable
    data object Loading : FavoriteState

    fun toBoolean(): Boolean {
        return when (this) {
            is Favorite -> true
            is NotFavorite -> false
            is Loading -> false
        }
    }

    companion object {
        fun from(isFavorite: Boolean) = if (isFavorite) {
            Favorite
        } else {
            NotFavorite
        }
    }
}