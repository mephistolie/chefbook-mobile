package com.cactusknights.chefbook.ui.screens.shoppinglist.views

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.cactusknights.chefbook.ui.screens.shoppinglist.models.presentation.ShoppingListSection
import com.cactusknights.chefbook.ui.themes.ChefBookTheme
import com.cactusknights.chefbook.ui.views.buttons.DynamicButton
import com.mephistolie.compost.extensions.Shading
import com.mephistolie.compost.modifiers.clippedBackground
import com.mephistolie.compost.modifiers.scalingClickable

@Composable
fun ShoppingListSection(
    state: ShoppingListSection,
    onPurchaseClicked: (String) -> Unit,
    onRecipeOpen: (Int) -> Unit,
    onAddPurchase: (Int?) -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = ChefBookTheme.colors
    val typography = ChefBookTheme.typography

    val backgroundShape = if (state.recipeId == null) RoundedCornerShape(28.dp, 28.dp, 0.dp, 0.dp) else RoundedCornerShape(28.dp)

    Column(
        modifier = modifier
            .padding(bottom = 4.dp)
            .fillMaxWidth()
            .wrapContentHeight()
            .clippedBackground(background = colors.backgroundPrimary, backgroundShape)
    ) {
        if (!state.title.isNullOrBlank()) {
            Box {
                val pressed = remember { mutableStateOf(false) }
                Text(
                    text = state.title,
                    style = typography.h3,
                    color = colors.foregroundPrimary,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp)
                        .scalingClickable(
                            scaleFactor = 1F,
                            pressed = pressed,
                        ) {
                            if (state.recipeId != null) onRecipeOpen(state.recipeId)
                        }
                )
                Shading(isVisible = pressed.value, color = Color.Black.copy(alpha = 0.1F))
            }
            Divider(
                color = colors.backgroundSecondary,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp)
                    .height(1.dp)
            )
        } else {
            Spacer(modifier = Modifier.height(16.dp))
        }
        state.purchases.forEachIndexed { index, purchase ->
            Purchase(
                purchase = purchase,
                modifier = Modifier.padding(16.dp, 0.dp, 16.dp, 16.dp)
            )
        }
        if (state.recipeId == null) {
            DynamicButton(
                text = "Добавить",
                onClick = { onAddPurchase(null) },
                isSelected = false,
                cornerRadius = 16.dp,
                unselectedForeground = colors.foregroundPrimary,
                modifier = Modifier
                    .navigationBarsPadding()
                    .padding(
                        start = 12.dp,
                        end = 12.dp,
                    )
                    .fillMaxWidth()
                    .height(56.dp),
            )
        }
    }
}