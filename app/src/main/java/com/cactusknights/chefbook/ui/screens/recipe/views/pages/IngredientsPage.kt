package com.cactusknights.chefbook.ui.screens.recipe.views.pages

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.cactusknights.chefbook.R
import com.mysty.chefbook.api.recipe.domain.entities.ingredient.IngredientItem
import com.cactusknights.chefbook.ui.screens.recipe.views.elements.Ingredient
import com.cactusknights.chefbook.ui.screens.recipe.views.elements.Section
import com.mysty.chefbook.core.ui.compose.providers.theme.LocalTheme
import com.mysty.chefbook.design.components.buttons.Counter
import com.mysty.chefbook.design.components.buttons.DynamicButton
import com.mephistolie.compost.modifiers.simpleClickable
import com.mephistolie.compost.shapes.DashedLineShape

@Composable
fun IngredientsPage(
    ingredients: List<IngredientItem>,
    servings: Int? = null,
    onAddToShoppingListClicked: (List<IngredientItem>, Float) -> Unit,
) {
    val colors = LocalTheme.colors
    val typography = LocalTheme.typography

    val selectedIngredients = remember { ingredients.map { false }.toMutableStateList() }

    val servingsMultiplier = remember { mutableStateOf(servings ?: 1) }
    val amountRatio = servingsMultiplier.value.toFloat() / (servings?.toFloat()
        ?: servingsMultiplier.value.toFloat())

    val isAddButtonVisible = selectedIngredients.any { isChecked -> isChecked }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp)
            .wrapContentHeight()
    ) {
        Spacer(modifier = Modifier.height(12.dp))
        if (ingredients.any { it is IngredientItem.Ingredient && it.amount != null }) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = stringResource(
                        if (servings != null)
                            R.string.common_general_servings
                        else
                            R.string.common_general_multiplier
                    ),
                    style = typography.headline1,
                    color = colors.foregroundSecondary,
                )
                Counter(
                    count = servingsMultiplier.value,
                    onMinusClicked = { if (servingsMultiplier.value > 1) servingsMultiplier.value -= 1 },
                    onPlusClicked = { if (servingsMultiplier.value < 99) servingsMultiplier.value += 1 },
                    isMultiplier = servings == null,
                )
            }
            Divider(
                color = colors.backgroundSecondary,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp)
                    .height(1.dp)
            )
        }
        ingredients.forEachIndexed { index, item ->
            when (item) {
                is IngredientItem.Section -> {
                    Section(item.name)
                    Spacer(modifier = Modifier.height(12.dp))
                }
                is IngredientItem.Ingredient -> {
                    Ingredient(
                        ingredient = item,
                        isChecked = selectedIngredients[index],
                        amountRatio = amountRatio,
                        modifier = Modifier.simpleClickable { selectedIngredients[index] = !selectedIngredients[index] }
                    )
                    if (index + 1 < ingredients.size) {
                        when (ingredients[index + 1]) {
                            is IngredientItem.Section -> {
                                Divider(
                                    color = colors.backgroundSecondary,
                                    modifier = Modifier
                                        .padding(top = 18.dp, bottom = 12.dp)
                                        .fillMaxWidth()
                                        .height(1.dp)
                                )
                            }
                            is IngredientItem.Ingredient -> {
                                Box(
                                    modifier = Modifier
                                        .padding(
                                            start = 32.dp,
                                            top = 12.dp,
                                            bottom = 12.dp,
                                        )
                                        .height((1.5).dp)
                                        .fillMaxWidth()
                                        .background(
                                            color = colors.backgroundSecondary,
                                            shape = DashedLineShape(12.dp)
                                        )
                                )
                            }
                            else -> Unit
                        }
                    } else {
                        Spacer(modifier = Modifier.height(18.dp))
                    }
                }
                else -> Unit
            }
        }
        AnimatedVisibility(
            visible = isAddButtonVisible,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            DynamicButton(
                leftIcon = ImageVector.vectorResource(R.drawable.ic_add),
                text = stringResource(R.string.common_recipe_screen_add_to_shopping_list).uppercase(),
                isSelected = true,
                modifier = Modifier
                    .wrapContentWidth()
                    .height(48.dp),
                onClick = {
                    val confirmedIngredients = ingredients
                        .filterIndexed { index, _ -> selectedIngredients[index] }
                    onAddToShoppingListClicked(confirmedIngredients, amountRatio)
                    selectedIngredients.replaceAll { false }
                },
            )
        }
        Spacer(modifier = Modifier.navigationBarsPadding().height(32.dp))
    }
}
