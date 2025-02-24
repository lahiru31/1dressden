package com.khadar3344.myshop.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp

object ResponsiveUtils {
    @Composable
    @ReadOnlyComposable
    fun calculateTextSize(baseSize: Int): TextUnit {
        val screenWidthDp = LocalConfiguration.current.screenWidthDp
        val scaleFactor = screenWidthDp / 360f
        return (baseSize * scaleFactor).coerceAtLeast(baseSize.toFloat()).sp
    }
}
