package com.example.test.ui.screen.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun UserItem(
    name: String,
    onClick: () -> Unit
) = Text(
    modifier = Modifier
        .defaultMinSize(minHeight = 48.dp)
        .clickable(onClick = onClick),
    text = name,
    style = MaterialTheme.typography.labelLarge
)