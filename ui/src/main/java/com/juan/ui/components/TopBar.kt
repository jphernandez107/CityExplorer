package com.juan.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
internal fun TopBar(
    title: String,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    showBackButton: Boolean = true,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        if (showBackButton) {
            IconButton(
                onClick = onBackClick,
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back Button",
                )
            }
        }
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
        )
    }
}

@Preview(showBackground = true)
@Composable
internal fun TopBarPreview() {
    TopBar(
        title = "City Details",
        showBackButton = true,
        onBackClick = {},
    )
}