package com.mysty.chefbook.ui.common.screens.recipe

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
import com.mysty.chefbook.api.recipe.domain.entities.RecipeInfo
import com.mysty.chefbook.core.android.compose.providers.theme.LocalTheme
import com.mysty.chefbook.design.components.progress.CircularProgressIndicator
import com.mysty.chefbook.design.components.toolbar.Toolbar
import com.mysty.chefbook.ui.common.components.recipe.RecipesGrid

@Composable
fun RecipesScreen(
    recipes: List<RecipeInfo>?,
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
