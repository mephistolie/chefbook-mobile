package io.chefbook.ui.common.providers

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import io.chefbook.design.components.bottomsheet.BottomSheetExpandProgressProvider

@Composable
fun BottomSheetExpandProgressProvider(
    progress: BottomSheetExpandProgressProvider,
    content: @Composable () -> Unit,
) =
    CompositionLocalProvider(
        LocalBottomSheetExpandProgressProvider provides progress,
        content = content
    )

val LocalBottomSheetExpandProgressProvider = staticCompositionLocalOf<BottomSheetExpandProgressProvider> {
    object : BottomSheetExpandProgressProvider {
        override val expandProgress: Float = 1F
    }
}
