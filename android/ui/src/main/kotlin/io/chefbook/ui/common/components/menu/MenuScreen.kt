package io.chefbook.ui.common.components.menu

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import io.chefbook.core.android.compose.providers.theme.LocalTheme

@Composable
fun MenuScreen(
  toolbar: @Composable () -> Unit = {},
  content: @Composable ColumnScope.() -> Unit,
) {
  val colors = LocalTheme.colors

  Column(
    modifier = Modifier
      .fillMaxSize()
      .background(colors.divider),
    horizontalAlignment = Alignment.CenterHorizontally,
  ) {
    toolbar()
    Column(
      modifier = Modifier
        .imePadding()
        .verticalScroll(rememberScrollState())
    ) {
      content()
    }
    Spacer(
      modifier = Modifier
        .weight(1F)
        .fillMaxSize()
        .background(colors.backgroundPrimary)
    )
  }
}