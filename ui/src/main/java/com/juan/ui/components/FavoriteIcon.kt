package com.juan.ui.components

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.juan.ui.citylist.FavoriteState

@Composable
internal fun FavoriteIcon(favoriteState: FavoriteState) {
    when (favoriteState) {
        is FavoriteState.Loading ->
            CircularProgressIndicator(
                modifier = Modifier.size(24.dp),
                strokeWidth = 2.dp,
            )
        is FavoriteState.NotFavorite ->
            Icon(
                imageVector = Icons.Outlined.FavoriteBorder,
                contentDescription = "Toggle Favorite"
            )
        is FavoriteState.Favorite ->
            Icon(
                imageVector = Icons.Outlined.Favorite,
                contentDescription = "Toggle Favorite"
            )
    }
}