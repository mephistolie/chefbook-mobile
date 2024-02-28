package io.chefbook.features.recipebook.categories.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.mephistolie.compost.modifiers.padding
import io.chefbook.core.android.compose.providers.theme.LocalTheme
import io.chefbook.core.android.R as coreR
import io.chefbook.design.components.toolbar.Toolbar
import io.chefbook.features.recipebook.categories.R
import io.chefbook.features.recipebook.categories.ui.mvi.CategoriesScreenIntent
import io.chefbook.features.recipebook.categories.ui.mvi.CategoriesScreenState
import io.chefbook.features.recipebook.core.ui.components.CategoryCard
import io.chefbook.ui.common.components.menu.MenuGroup

@Composable
internal fun CategoriesScreenContent(
  state: CategoriesScreenState,
  onIntent: (CategoriesScreenIntent) -> Unit,
) {
  val colors = LocalTheme.colors
  val typography = LocalTheme.typography

  Column(
    modifier = Modifier
      .background(colors.backgroundPrimary)
      .statusBarsPadding()
      .padding(horizontal = 12.dp)
      .fillMaxSize(),
  ) {
    Toolbar(
      onLeftButtonClick = { onIntent(CategoriesScreenIntent.Back) },
      content = {
        Text(
          text = stringResource(R.string.common_categories_screen_categories_and_tags),
          style = typography.h4,
          color = colors.foregroundPrimary,
        )
      },
    )
    LazyVerticalGrid(
      columns = GridCells.Fixed(4),
      modifier = Modifier.fillMaxSize(),
    ) {
      if (state.categories.isNotEmpty()) {
        item(span = { GridItemSpan(4) }) {
          Text(
            text = stringResource(coreR.string.common_general_categories),
            style = typography.h2,
            color = colors.foregroundPrimary,
            modifier = Modifier
              .fillMaxSize()
              .padding(top = 20.dp, bottom = 16.dp)
              .wrapContentHeight()
          )
        }
        itemsIndexed(state.categories) { index, category ->
          CategoryCard(
            id = category.id,
            name = category.name,
            emoji = category.emoji,
            modifier = Modifier
              .padding(
                start = if (index % 4 == 0) 0.dp else 4.dp,
                end = if (index % 4 == 3) 0.dp else 4.dp,
                bottom = 8.dp,
              )
          ) { onIntent(CategoriesScreenIntent.CategoryClicked(it)) }
        }
      }
      if (state.tags.isNotEmpty()) {
        item(span = { GridItemSpan(4) }) {
          Text(
            text = stringResource(coreR.string.common_general_tags),
            style = typography.h2,
            color = colors.foregroundPrimary,
            modifier = Modifier
              .fillMaxSize()
              .padding(top = 20.dp, bottom = 16.dp)
              .wrapContentHeight()
          )
        }
        itemsIndexed(state.tags) { index, tag ->
          CategoryCard(
            id = tag.id,
            name = tag.name,
            emoji = tag.emoji,
            modifier = Modifier
              .padding(
                start = if (index % 4 == 0) 0.dp else 4.dp,
                end = if (index % 4 == 3) 0.dp else 4.dp,
                bottom = 8.dp,
              )
          ) { onIntent(CategoriesScreenIntent.TagClicked(it)) }
        }
      }
    }
  }
}
