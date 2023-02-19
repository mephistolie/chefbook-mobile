package com.mysty.chefbook.core.android.compose.providers.ui

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

data class UiSizes(
    val statusBarHeight: Dp = 0.dp,
    val navigationBarsPadding: PaddingValues = PaddingValues(),
)