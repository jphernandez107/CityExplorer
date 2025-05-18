package com.juan.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.juan.ui.citylist.SearchBarViewState

@Composable
internal fun CitySearchBar(
    searchBarViewState: SearchBarViewState,
    onQueryChanged: (String) -> Unit,
    onFavoriteToggle: () -> Unit,
    onClearClick: () -> Unit,
) {
    val horizontalPadding = 16.dp
    Column(
        modifier = Modifier
            .fillMaxWidth(),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = horizontalPadding, end = horizontalPadding, top = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            TextField(
                value = searchBarViewState.searchQuery,
                onValueChange = onQueryChanged,
                modifier = Modifier.weight(1f),
                placeholder = { Text("Search cities...") },
                singleLine = true,
                trailingIcon = {
                    TrailingIcon(
                        showClearButton = searchBarViewState.searchQuery.isNotEmpty(),
                        onClearClick = onClearClick,
                    )
                }
            )
        }
        LazyRow (
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            contentPadding = PaddingValues(horizontal = 16.dp)
        ) {
            item(key = "only_favorites_option") {
                val selected = searchBarViewState.onlyFavorites
                FilterOption(
                    selected = selected,
                    icon = if (selected) {
                        Icons.Default.Favorite
                    } else {
                        Icons.Outlined.FavoriteBorder
                    },
                    onFavoriteToggle = onFavoriteToggle,
                )
            }
        }
    }
}

@Composable
private fun TrailingIcon(
    modifier: Modifier = Modifier,
    showClearButton: Boolean,
    onClearClick: () -> Unit,
) {
    val icon = if (showClearButton) {
        Icons.Default.Clear
    } else {
        Icons.Default.Search
    }
    IconButton(
        onClick = onClearClick,
        enabled = showClearButton,
        colors = IconButtonDefaults.iconButtonColors().copy(
            contentColor = IconButtonDefaults.iconButtonColors().contentColor,
            disabledContentColor = IconButtonDefaults.iconButtonColors().contentColor,
        )
    ) {
        Icon(
            imageVector = icon,
            contentDescription = "Trailing icon",
            tint = IconButtonDefaults.iconButtonColors().contentColor,
            modifier = modifier
                .padding(8.dp)
        )
    }
}

@Composable
private fun FilterOption(
    selected: Boolean,
    icon: ImageVector,
    onFavoriteToggle: () -> Unit,
) {
    FilterChip(
        selected = selected,
        onClick = onFavoriteToggle,
        label = { Text("Only Favorites") },
        leadingIcon = {
            Icon(
                imageVector = icon,
                contentDescription = null,
            )
        },
        shape = RoundedCornerShape(8.dp),
    )
}

@Preview(showBackground = true)
@Composable
private fun CitySearchBarPreview() = MaterialTheme {
    var searchQuery by remember { mutableStateOf("Madrid") }
    CitySearchBar(
        searchBarViewState = SearchBarViewState(
            searchQuery = searchQuery,
            onlyFavorites = true,
        ),
        onQueryChanged = { searchQuery = it },
        onFavoriteToggle = {},
        onClearClick = { searchQuery = "" },
    )
}