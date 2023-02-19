package com.mysty.chefbook.features.recipebook.search.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.google.accompanist.flowlayout.FlowRow
import com.mephistolie.compost.modifiers.simpleClickable
import com.mysty.chefbook.core.android.compose.providers.theme.LocalTheme
import com.mysty.chefbook.design.components.buttons.DynamicButton
import com.mysty.chefbook.features.recipebook.search.R
import com.mysty.chefbook.features.recipebook.search.ui.components.SearchRecipeCard
import com.mysty.chefbook.features.recipebook.search.ui.mvi.RecipeBookSearchScreenIntent
import com.mysty.chefbook.features.recipebook.search.ui.mvi.RecipeBookSearchScreenState

@OptIn(ExperimentalComposeUiApi::class)
@Composable
internal fun RecipeBookSearchScreenContent(
    state: RecipeBookSearchScreenState,
    onIntent: (RecipeBookSearchScreenIntent) -> Unit,
) {
    val keyboardController = LocalSoftwareKeyboardController.current

    val colors = LocalTheme.colors
    val typography = LocalTheme.typography

    val focusRequester = remember { FocusRequester() }

    Box(
        modifier = Modifier
            .statusBarsPadding()
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .padding(12.dp, 8.dp, 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    modifier = Modifier
                        .weight(1F)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    BasicTextField(
                        value = state.query,
                        onValueChange = { query: String ->
                            onIntent(
                                RecipeBookSearchScreenIntent.Search(
                                    query
                                )
                            )
                        },
                        keyboardActions = KeyboardActions(
                            onDone = { keyboardController?.hide() }
                        ),
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .focusRequester(focusRequester),
                        textStyle = typography.body1.copy(color = colors.foregroundPrimary),
                        cursorBrush = SolidColor(colors.tintPrimary),
                        decorationBox = { innerTextField ->
                            Row(modifier = Modifier.fillMaxWidth()) {
                                if (state.query.isEmpty()) {
                                    Text(
                                        text = stringResource(R.string.common_general_search),
                                        style = typography.body1,
                                        color = colors.foregroundSecondary,
                                    )
                                }
                            }
                            innerTextField()
                        }
                    )
                }
                Text(
                    text = stringResource(R.string.common_general_cancel),
                    modifier = Modifier
                        .padding(start = 8.dp)
                        .simpleClickable { onIntent(RecipeBookSearchScreenIntent.Back) },
                    style = typography.h4,
                    color = colors.tintPrimary,
                )
            }
            Divider(
                color = colors.backgroundTertiary,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp),
            )
            if (!state.isLoading) {
                LazyColumn(
                    modifier = Modifier
                        .padding(12.dp, 12.dp, 12.dp)
                        .wrapContentHeight(),
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                ) {
                    if (state.categories.isNotEmpty()) {
                        item {
                            FlowRow(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 4.dp),
                                mainAxisSpacing = 8.dp,
                                crossAxisSpacing = 8.dp,
                            ) {
                                for (category in state.categories) {
                                    DynamicButton(
                                        text = "${category.cover} ${category.name}".trim(),
                                        onClick = { onIntent(RecipeBookSearchScreenIntent.OpenCategoryScreen(category.id)) },
                                        modifier = Modifier.height(38.dp),
                                        horizontalPadding = 8.dp,
                                        selectedBackground = colors.foregroundPrimary,
                                        unselectedForeground = colors.foregroundPrimary,
                                    )
                                }
                            }
                        }
                    }
                    items(state.recipes) { recipe ->
                        SearchRecipeCard(recipe) {
                            onIntent(RecipeBookSearchScreenIntent.OpenRecipeScreen(recipe.id))
                        }
                    }
                }
                if (state.query.isNotEmpty() && state.recipes.isEmpty() && state.categories.isEmpty()) {
                    Image(
                        imageVector = ImageVector.vectorResource(R.drawable.ic_broccy_think),
                        contentDescription = null,
                        modifier = Modifier
                            .padding(top = 48.dp)
                            .size(144.dp)
                    )
                    Text(
                        text = stringResource(R.string.common_general_nothing_found),
                        style = typography.headline1,
                        color = colors.foregroundSecondary
                    )
                }
            }
        }
        if (state.isLoading) {
            CircularProgressIndicator(
                color = colors.tintPrimary,
                modifier = Modifier.size(36.dp)
            )
        }
    }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }
}


