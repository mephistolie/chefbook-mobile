package io.chefbook.design.components.bottomsheet

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.mephistolie.compost.modifiers.clippedBackground
import io.chefbook.core.android.compose.providers.theme.LocalTheme
import io.chefbook.design.theme.shapes.ModalBottomSheetShape

@Composable
inline fun BottomSheetColumn(
  modifier: Modifier = Modifier,
  backgroundColor: Color = LocalTheme.colors.backgroundPrimary,
  pullBarType: PullBarType = PullBarType.NONE,
  content: @Composable ColumnScope.() -> Unit,
) {
  when (pullBarType) {
    PullBarType.NONE -> Column(
      modifier = modifier
        .fillMaxWidth()
        .statusBarsPadding()
        .clippedBackground(
          background = backgroundColor,
          shape = ModalBottomSheetShape,
        ),
      horizontalAlignment = Alignment.CenterHorizontally,
      content = content,
    )

    PullBarType.INTERNAL -> Column(
      modifier = modifier
        .fillMaxWidth()
        .statusBarsPadding()
        .clippedBackground(
          background = backgroundColor,
          shape = ModalBottomSheetShape,
        ),
      horizontalAlignment = Alignment.CenterHorizontally,
    ) {
      PullBar()
      content()
    }

    PullBarType.EXTERNAl -> Column(
      modifier = modifier,
      horizontalAlignment = Alignment.CenterHorizontally,
    ) {
      PullBar(
        modifier = Modifier.statusBarsPadding(),
        isInverted = true,
      )
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .clippedBackground(
            background = backgroundColor,
            shape = ModalBottomSheetShape,
          ),
        horizontalAlignment = Alignment.CenterHorizontally,
        content = content,
      )
    }
  }
}