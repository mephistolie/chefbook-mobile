package com.cactusknights.chefbook.ui.screens.recipeinput.screens.details.views.blocks

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.cactusknights.chefbook.R
import com.cactusknights.chefbook.core.ui.localizedName
import com.cactusknights.chefbook.domain.entities.common.Visibility
import com.cactusknights.chefbook.domain.entities.recipe.RecipeInput
import com.cactusknights.chefbook.ui.themes.ChefBookTheme
import com.cactusknights.chefbook.ui.views.buttons.DynamicButton

@Composable
fun ParametersBlock(
    state: RecipeInput,
    onVisibilityClick: () -> Unit,
    onLanguageClick: () -> Unit,
    onEncryptionClick: () -> Unit,
) {
    val resources = LocalContext.current.resources

    val colors = ChefBookTheme.colors
    val typography = ChefBookTheme.typography

    LazyRow(
        modifier = Modifier.padding(top = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        item {
            DynamicButton(
                text = when (state.visibility) {
                    Visibility.PRIVATE -> stringResource(R.string.common_general_visible_to_you)
                    Visibility.SHARED -> stringResource(R.string.common_general_visible_by_link)
                    Visibility.PUBLIC -> stringResource(R.string.common_general_visible_to_everyone)
                },
                horizontalPadding = 10.dp,
                cornerRadius = 10.dp,
                textStyle = typography.body2,
                leftIcon = ImageVector.vectorResource(
                    when (state.visibility) {
                        Visibility.PRIVATE -> R.drawable.ic_eye_closed
                        Visibility.SHARED -> R.drawable.ic_link
                        Visibility.PUBLIC -> R.drawable.ic_earth
                    }
                ),
                iconsSize = 16.dp,
                rightIcon = ImageVector.vectorResource(R.drawable.ic_arrow_down),
                unselectedForeground = colors.foregroundPrimary,
                modifier = Modifier
                    .padding(start = 12.dp)
                    .height(36.dp),
                onClick = onVisibilityClick,
            )
        }
        item {
            DynamicButton(
                text = "${state.language.flag} ${
                    state.language.localizedName(
                        resources
                    )
                }",
                horizontalPadding = 10.dp,
                cornerRadius = 10.dp,
                textStyle = typography.body2,
                iconsSize = 16.dp,
                rightIcon = ImageVector.vectorResource(R.drawable.ic_arrow_down),
                unselectedForeground = colors.foregroundPrimary,
                modifier = Modifier.height(36.dp),
                onClick = onLanguageClick,
            )
        }
        item {
            DynamicButton(
                text = stringResource(if (state.isEncrypted) R.string.common_general_encrypted else R.string.common_general_standard),
                horizontalPadding = 10.dp,
                cornerRadius = 10.dp,
                textStyle = typography.body2,
                iconsSize = 16.dp,
                leftIcon = ImageVector.vectorResource(if (state.isEncrypted) R.drawable.ic_lock else R.drawable.ic_lock_open),
                rightIcon = ImageVector.vectorResource(R.drawable.ic_arrow_down),
                unselectedForeground = colors.foregroundPrimary,
                modifier = Modifier
                    .padding(end = 12.dp)
                    .height(36.dp),
                onClick = onEncryptionClick,
            )
        }
    }
}