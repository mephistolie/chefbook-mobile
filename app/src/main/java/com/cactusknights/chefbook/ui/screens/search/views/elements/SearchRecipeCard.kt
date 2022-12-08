package com.cactusknights.chefbook.ui.screens.search.views.elements

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.cactusknights.chefbook.R
import com.cactusknights.chefbook.common.Utils
import com.cactusknights.chefbook.core.ui.RecipeEncryptionProvider
import com.cactusknights.chefbook.domain.entities.recipe.RecipeInfo
import com.cactusknights.chefbook.ui.themes.ChefBookTheme
import com.cactusknights.chefbook.ui.views.images.EncryptedImage
import com.mephistolie.compost.extensions.Shading
import com.mephistolie.compost.modifiers.clippedBackground
import com.mephistolie.compost.modifiers.scalingClickable

@Composable
fun SearchRecipeCard(
    recipe: RecipeInfo,
    modifier: Modifier = Modifier,
    onRecipeClicked: (RecipeInfo) -> Unit,
) {
    val context = LocalContext.current

    val colors = ChefBookTheme.colors
    val typography = ChefBookTheme.typography

    val pressed = remember { mutableStateOf(false) }

    RecipeEncryptionProvider(encryption = recipe.encryptionState) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .height(48.dp)
                .scalingClickable(pressed) { onRecipeClicked(recipe) },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .padding(end = 8.dp)
                    .size(48.dp)
                    .clippedBackground(colors.backgroundSecondary, RoundedCornerShape(10.dp))
            ) {
                EncryptedImage(recipe.preview ?: R.drawable.ic_broccy,)
                Shading(pressed.value)
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
            ) {
                Text(
                    text = recipe.name,
                    style = typography.headline1,
                    maxLines = 2,
                    color = colors.foregroundPrimary,
                )
                recipe.time?.let {
                    Text(
                        text = Utils.minutesToTimeString(recipe.time, context.resources),
                        style = typography.body2,
                        color = colors.foregroundSecondary
                    )
                }
            }
        }
    }
}
