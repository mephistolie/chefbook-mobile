package io.chefbook.features.community.recipes.ui.screens.filter.components.blocks

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.mephistolie.compost.modifiers.padding
import io.chefbook.core.compose.providers.theme.LocalTheme
import io.chefbook.design.components.textfields.OutlinedTextField
import io.chefbook.design.theme.shapes.SmoothCornerShape28
import io.chefbook.core.res as CoreR

internal fun LazyListScope.searchBlock(
  search: String,
  onSearchChange: (String) -> Unit,
  modifier: Modifier = Modifier,
  focusSearch: Boolean = false,
) {
  item {
    val colors = LocalTheme.colors
    val typography = LocalTheme.typography

    val focusRequester = remember { FocusRequester() }

    Column(
      modifier = modifier
        .padding(bottom = 8.dp)
        .background(colors.backgroundPrimary, SmoothCornerShape28)
        .padding(12.dp)
    ) {
      Text(
        text = stringResource(CoreR.string.common_general_search),
        style = typography.h2,
        color = colors.foregroundPrimary,
        modifier = Modifier
          .padding(horizontal = 4.dp, top = 4.dp, bottom = 16.dp)
          .wrapContentHeight()
      )
      OutlinedTextField(
        value = search,
        onValueChange = onSearchChange,
        modifier = modifier
          .fillMaxWidth()
          .focusRequester(focusRequester),
        hint = stringResource(CoreR.string.common_general_name),
      )
    }

    LaunchedEffect(focusSearch) {
      if (focusSearch) focusRequester.requestFocus()
    }
  }
}
