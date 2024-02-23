package io.chefbook.features.recipebook.core.ui.screens.recipe

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.chefbook.core.android.compose.providers.theme.LocalTheme
import io.chefbook.design.components.progress.CircularProgressIndicator
import io.chefbook.design.components.toolbar.Toolbar
import io.chefbook.features.recipebook.core.ui.components.RecipesGrid
import io.chefbook.sdk.recipe.core.api.external.domain.entities.DecryptedRecipeInfo

@Composable
fun RecipesScreen(
  recipes: List<DecryptedRecipeInfo>?,
  onRecipeClick: (String) -> Unit,
  onBack: () -> Unit,
  onToolbarContentClick: () -> Unit = {},
  toolbarContent: @Composable ColumnScope.() -> Unit,
) {
  val colors = LocalTheme.colors

  Box(
    modifier = Modifier
      .background(colors.backgroundPrimary)
      .statusBarsPadding()
      .padding(horizontal = 12.dp)
      .fillMaxSize(),
  ) {
    if (recipes != null) {
      Column(
        horizontalAlignment = Alignment.CenterHorizontally
      ) {
        Toolbar(
          onLeftButtonClick = onBack,
          onContentClick = onToolbarContentClick,
          content = toolbarContent,
        )
        RecipesGrid(
          recipes = recipes,
          onRecipeClick = onRecipeClick,
        )
      }
    } else {
      CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
    }
  }
}
