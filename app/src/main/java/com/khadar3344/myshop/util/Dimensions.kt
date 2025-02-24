package com.khadar3344.myshop.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

object Dimensions {
    private val screenWidth
        @Composable
        @ReadOnlyComposable
        get() = LocalConfiguration.current.screenWidthDp.dp

    // Spacing values
    val spacing_small
        @Composable
        @ReadOnlyComposable
        get() = (screenWidth.value * 0.02f).dp.coerceAtLeast(4.dp)

    val spacing_medium
        @Composable
        @ReadOnlyComposable
        get() = (screenWidth.value * 0.04f).dp.coerceAtLeast(8.dp)

    val spacing_large
        @Composable
        @ReadOnlyComposable
        get() = (screenWidth.value * 0.06f).dp.coerceAtLeast(16.dp)

    val spacing_xlarge
        @Composable
        @ReadOnlyComposable
        get() = (screenWidth.value * 0.08f).dp.coerceAtLeast(32.dp)

    // Text sizes
    val text_small
        @Composable
        @ReadOnlyComposable
        get() = ResponsiveUtils.calculateTextSize(12)

    val text_medium
        @Composable
        @ReadOnlyComposable
        get() = ResponsiveUtils.calculateTextSize(14)

    val text_large
        @Composable
        @ReadOnlyComposable
        get() = ResponsiveUtils.calculateTextSize(16)

    val text_xlarge
        @Composable
        @ReadOnlyComposable
        get() = ResponsiveUtils.calculateTextSize(20)

    val text_title
        @Composable
        @ReadOnlyComposable
        get() = ResponsiveUtils.calculateTextSize(24)

    // Component sizes
    val button_height
        @Composable
        @ReadOnlyComposable
        get() = (48f * (screenWidth.value / 360f)).dp.coerceAtLeast(40.dp)

    val input_field_height
        @Composable
        @ReadOnlyComposable
        get() = (56f * (screenWidth.value / 360f)).dp.coerceAtLeast(48.dp)

    val image_size_small
        @Composable
        @ReadOnlyComposable
        get() = (screenWidth.value * 0.1f).dp.coerceAtLeast(32.dp)

    val image_size_medium
        @Composable
        @ReadOnlyComposable
        get() = (screenWidth.value * 0.2f).dp.coerceAtLeast(64.dp)

    val image_size_large
        @Composable
        @ReadOnlyComposable
        get() = (screenWidth.value * 0.3f).dp.coerceAtLeast(96.dp)

    // Grid columns based on screen width
    val gridColumns
        @Composable
        @ReadOnlyComposable
        get() = when {
            screenWidth < 600.dp -> 2
            screenWidth < 840.dp -> 3
            else -> 4
        }

    // Dynamic padding for different screen sizes
    val screenPadding
        @Composable
        @ReadOnlyComposable
        get() = when {
            screenWidth < 600.dp -> 16.dp
            screenWidth < 840.dp -> 24.dp
            else -> 32.dp
        }
}
