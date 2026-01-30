package com.clouditemapp.presentation.ui.common

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp

enum class WindowSizeClass {
    Compact, Medium, Expanded
}

@Composable
fun rememberWindowSizeClass(): WindowSizeClass {
    val configuration = LocalConfiguration.current
    return when {
        configuration.screenWidthDp < 600 -> WindowSizeClass.Compact
        configuration.screenWidthDp < 840 -> WindowSizeClass.Medium
        else -> WindowSizeClass.Expanded
    }
}
