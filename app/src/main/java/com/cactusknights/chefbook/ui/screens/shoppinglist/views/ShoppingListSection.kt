package com.cactusknights.chefbook.ui.screens.shoppinglist.views

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.cactusknights.chefbook.R
import com.cactusknights.chefbook.ui.screens.shoppinglist.models.presentation.ShoppingListSection
import com.cactusknights.chefbook.ui.themes.ChefBookTheme
import com.mephistolie.compost.extensions.Shading
import com.mephistolie.compost.modifiers.clippedBackground
import com.mephistolie.compost.modifiers.scalingClickable
import com.mephistolie.compost.modifiers.simpleClickable

@Composable
fun ShoppingListSection(
    state: ShoppingListSection,
    onPurchaseClicked: (String) -> Unit,
    onRecipeOpen: (String) -> Unit,
    onAddPurchase: (Int?) -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = ChefBookTheme.colors
    val typography = ChefBookTheme.typography

    Column(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .clippedBackground(background = colors.backgroundPrimary, RoundedCornerShape(28.dp))
            .animateContentSize()
    ) {
        if (!state.title.isNullOrBlank()) {
            Box {
                val pressed = remember { mutableStateOf(false) }
                Row(
                    modifier = Modifier
                        .scalingClickable(
                            scaleFactor = 1F,
                            pressed = pressed,
                        ) {
                            if (state.recipeId != null) onRecipeOpen(state.recipeId)
                        }
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = state.title,
                        style = typography.h3,
                        color = colors.foregroundPrimary,
                        modifier = Modifier
                            .weight(1F)
                            .fillMaxWidth()
                    )
                    Icon(
                        painter = painterResource(id = R.drawable.ic_arrow_right),
                        contentDescription = null,
                        tint = colors.foregroundPrimary,
                        modifier = Modifier
                            .padding(end = 4.dp)
                            .size(18.dp)
                    )
                }
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
        state.purchases.forEachIndexed { _, purchase ->
            Purchase(
                purchase = purchase,
                modifier = Modifier
                    .simpleClickable { onPurchaseClicked(purchase.id) }
                    .padding(16.dp, 0.dp, 16.dp, 16.dp)
            )
        }
    }
}