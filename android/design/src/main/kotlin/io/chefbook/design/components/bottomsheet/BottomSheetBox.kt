package io.chefbook.design.components.bottomsheet

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.mephistolie.compost.modifiers.clippedBackground
import io.chefbook.core.android.compose.providers.theme.LocalTheme
import io.chefbook.design.theme.shapes.RoundedCornerShape28Top

@Composable
inline fun BottomSheetBox(
  modifier: Modifier = Modifier,
  backgroundColor: Color = LocalTheme.colors.backgroundPrimary,
  pullBarType: PullBarType = PullBarType.NONE,
  content: @Composable BoxScope.() -> Unit,
) {
  when (pullBarType) {
    PullBarType.NONE -> Box(
      modifier = modifier
        .fillMaxWidth()
        .statusBarsPadding()
        .clippedBackground(
          background = backgroundColor,
          shape = RoundedCornerShape28Top,
        ),
      contentAlignment = Alignment.TopCenter,
      content = content,
    )

    PullBarType.INTERNAL -> Column(
      modifier = modifier
        .fillMaxWidth()
        .statusBarsPadding()
        .clippedBackground(
          background = backgroundColor,
          shape = RoundedCornerShape28Top,
        ),
      horizontalAlignment = Alignment.CenterHorizontally,
    ) {
      PullBar()
      Box(
        contentAlignment = Alignment.TopCenter,
        content = content,
      )
    }

    PullBarType.EXTERNAl -> Column(
      modifier = modifier,
      horizontalAlignment = Alignment.CenterHorizontally,
    ) {
      PullBar(
        modifier = Modifier.statusBarsPadding(),
        isInverted = true,
      )
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .clippedBackground(
            background = backgroundColor,
            shape = RoundedCornerShape28Top,
          ),
        contentAlignment = Alignment.TopCenter,
        content = content,
      )
    }
  }
}