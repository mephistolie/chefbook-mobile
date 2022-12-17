package com.cactusknights.chefbook.ui.screens.recipeinput.screens.ingredients.views

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.cactusknights.chefbook.R
import com.mysty.chefbook.api.recipe.domain.entities.RecipeInput
import com.mysty.chefbook.api.recipe.domain.entities.ingredient.IngredientItem
import com.cactusknights.chefbook.ui.screens.recipeinput.models.RecipeInputScreenEvent
import com.cactusknights.chefbook.ui.screens.recipeinput.screens.ingredients.views.elements.IngredientField
import com.cactusknights.chefbook.ui.screens.recipeinput.screens.ingredients.views.elements.SectionField
import com.mysty.chefbook.core.ui.compose.providers.theme.LocalTheme
import com.mysty.chefbook.design.components.buttons.DynamicButton
import com.mysty.chefbook.design.components.toolbar.Toolbar
import org.burnoutcrew.reorderable.ReorderableItem
import org.burnoutcrew.reorderable.detectReorderAfterLongPress
import org.burnoutcrew.reorderable.rememberReorderableLazyListState
import org.burnoutcrew.reorderable.reorderable

@Composable
fun RecipeInputIngredientScreenDisplay(
    state: RecipeInput,
    onEvent: (RecipeInputScreenEvent) -> Unit,
) {
    val haptic = LocalHapticFeedback.current

    val colors = LocalTheme.colors
    val typography = LocalTheme.typography

    val reorderableState = rememberReorderableLazyListState({ from, to ->
        onEvent(RecipeInputScreenEvent.MoveIngredientItem(from.index, to.index))
    })

    Column(
        modifier = Modifier
            .statusBarsPadding()
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Toolbar(
            onLeftButtonClick = { onEvent(RecipeInputScreenEvent.Back) },
            modifier = Modifier.padding(horizontal = 12.dp)
        ) {
            Text(
                text = stringResource(R.string.common_general_ingredients),
                maxLines = 1,
                style = typography.h4,
                color = colors.foregroundPrimary,
            )
        }
        LazyColumn(
            state = reorderableState.listState,
            modifier = Modifier
                .weight(1F)
                .fillMaxHeight()
                .reorderable(reorderableState)
                .detectReorderAfterLongPress(reorderableState),
            horizontalAlignment = Alignment.Start,
        ) {
            items(items = state.ingredients, key = { item -> item.id }) { item ->
                ReorderableItem(reorderableState, key = item.id) { isDragging ->
                    if (isDragging) haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                    when (item) {
                        is IngredientItem.Section -> {
                            SectionField(
                                name = item.name,
                                onNameChange = { name -> onEvent(RecipeInputScreenEvent.SetIngredientItemName(item.id, name)) },
                                onDeleteClick = { onEvent(RecipeInputScreenEvent.DeleteIngredientItem(item.id)) },
                            )
                        }
                        is IngredientItem.Ingredient -> {
                            IngredientField(
                                ingredient = item,
                                onInputClick = { onEvent(RecipeInputScreenEvent.OpenIngredientDialog(item.id)) },
                                onDeleteClick = { onEvent(RecipeInputScreenEvent.DeleteIngredientItem(item.id)) },
                            )
                        }
                        else -> Unit
                    }
                }
            }
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp, 16.dp, 12.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    DynamicButton(
                        leftIcon = ImageVector.vectorResource(R.drawable.ic_add),
                        text = stringResource(R.string.common_general_section),
                        unselectedForeground = colors.foregroundPrimary,
                        modifier = Modifier
                            .height(36.dp),
                        onClick = { onEvent(RecipeInputScreenEvent.AddIngredientSection) }
                    )
                    DynamicButton(
                        leftIcon = ImageVector.vectorResource(R.drawable.ic_add),
                        text = stringResource(R.string.common_general_ingredient),
                        unselectedForeground = colors.foregroundPrimary,
                        modifier = Modifier
                            .height(36.dp),
                        onClick = { onEvent(RecipeInputScreenEvent.AddIngredient) }
                    )
                }
            }
            item {
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
        AnimatedVisibility(
            state.ingredients.any { it is IngredientItem.Ingredient && it.name.isNotBlank() }
        ) {
            DynamicButton(
                text = stringResource(R.string.common_general_continue),
                isSelected = true,
                cornerRadius = 16.dp,
                modifier = Modifier
                    .navigationBarsPadding()
                    .padding(
                        start = 12.dp,
                        top = 8.dp,
                        end = 12.dp,
                        bottom = 4.dp,
                    )
                    .fillMaxWidth()
                    .height(56.dp),
                onClick = { onEvent(RecipeInputScreenEvent.Continue) },
            )
        }
    }
}

