package com.cactusknights.chefbook.ui.screens.recipeinput.dialogs

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.cactusknights.chefbook.R
import com.mysty.chefbook.core.ui.compose.providers.theme.LocalTheme
import com.mysty.chefbook.design.components.buttons.DynamicButton
import com.mysty.chefbook.design.theme.ChefBookTheme
import com.mysty.chefbook.design.theme.shapes.RoundedCornerShape24

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun RecipeSavedDialog(
    onOpenRecipe: () -> Unit,
    onBackToRecipes: () -> Unit,
) {
    val colors = LocalTheme.colors
    val typography = LocalTheme.typography

    Dialog(
        onDismissRequest = {},
        properties = DialogProperties(
            dismissOnBackPress = false,
            dismissOnClickOutside = false,
            usePlatformDefaultWidth = false
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(horizontal = 48.dp)
                .background(color = colors.backgroundPrimary, shape = RoundedCornerShape24)
                .padding(16.dp, 24.dp, 16.dp, 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = stringResource(id = R.string.common_recipe_input_screen_recipe_saved),
                style = typography.h3,
                color = colors.foregroundPrimary
            )
            Image(
                imageVector = ImageVector.vectorResource(R.drawable.ic_broccy),
                contentDescription = null,
                modifier = Modifier
                    .padding(top = 12.dp)
                    .size(128.dp)
            )
            DynamicButton(
                text = stringResource(R.string.common_recipe_input_screen_open_recipe),
                isSelected = true,
                unselectedForeground = colors.foregroundPrimary,
                modifier = Modifier
                    .padding(top = 24.dp)
                    .fillMaxWidth()
                    .height(48.dp),
                onClick = onOpenRecipe,
            )
            DynamicButton(
                text = stringResource(R.string.common_recipe_input_screen_back_to_recipe_book),
                isSelected = false,
                unselectedForeground = colors.foregroundPrimary,
                modifier = Modifier
                    .padding(top = 8.dp)
                    .fillMaxWidth()
                    .height(48.dp),
                onClick = onBackToRecipes,
            )
        }
    }
}

@Composable
@Preview(showBackground = true)
private fun PreviewLightRecipeSavedDialog() {
    ThemedRecipeSavedDialog(false)
}

@Composable
@Preview(showBackground = true)
private fun PreviewDarkRecipeSavedDialog() {
    ThemedRecipeSavedDialog(true)
}

@Composable
private fun ThemedRecipeSavedDialog(
    isDarkTheme: Boolean
) {
    ChefBookTheme(darkTheme = isDarkTheme) {
        Surface(
            color = LocalTheme.colors.backgroundPrimary
        ) {
            RecipeSavedDialog(
                onOpenRecipe = {},
                onBackToRecipes = {},
            )
        }
    }
}