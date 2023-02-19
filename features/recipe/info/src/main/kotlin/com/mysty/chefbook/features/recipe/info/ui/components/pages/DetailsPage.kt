package com.mysty.chefbook.features.recipe.info.ui.components.pages

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.mysty.chefbook.api.common.entities.language.Language
import com.mysty.chefbook.api.recipe.domain.entities.visibility.Visibility
import com.mysty.chefbook.core.android.compose.providers.theme.LocalTheme
import com.mysty.chefbook.design.components.buttons.DynamicButton
import com.mysty.chefbook.features.recipe.info.R
import com.mysty.chefbook.features.recipe.info.ui.components.blocks.CategoriesBlock
import com.mysty.chefbook.features.recipe.info.ui.components.details.diet.DietWidget
import com.mysty.chefbook.features.recipe.info.ui.components.details.info.InfoElement
import com.mysty.chefbook.features.recipe.info.ui.mvi.RecipeScreenState
import com.mysty.chefbook.ui.common.extensions.localizedName
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

@Composable
internal  fun DetailsPage(
    state: RecipeScreenState.Success,
    onCategoryClicked: (String) -> Unit,
    onChangeCategoriesClicked: () -> Unit,
    onEditRecipeClicked: () -> Unit,
    onDeleteRecipeClicked: () -> Unit,
) {
    val resources = LocalContext.current.resources

    val colors = LocalTheme.colors
    val typography = LocalTheme.typography

    val recipe = state.recipe

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp)
            .wrapContentHeight()
    ) {
        Text(
            text = state.recipe.name,
            modifier = Modifier.padding(
                top = 12.dp,
                bottom = 4.dp
            ),
            maxLines = 3,
            style = typography.h2,
            color = colors.foregroundPrimary,
        )
        if (recipe.hasDietData()) {
            DietWidget(
                calories = recipe.calories,
                macronutrients = recipe.macronutrients,
            )
        }
        AnimatedVisibility(recipe.isSaved && state.categoriesForSelection == null) {
            CategoriesBlock(
                categories = recipe.categories,
                onChangeCategoriesButtonClicked = onChangeCategoriesClicked,
                onCategoryButtonClicked = onCategoryClicked,
            )
        }
        Spacer(modifier = Modifier.height(12.dp))
        recipe.description?.let { description ->
            InfoElement(
                name = stringResource(R.string.common_general_description),
                value = description,
                modifier = Modifier.padding(bottom = 12.dp),
            )
        }
        recipe.ownerName?.let { author ->
            InfoElement(
                name = stringResource(R.string.common_general_author),
                value = author,
                modifier = Modifier.padding(bottom = 12.dp),
            )
        }
        if (recipe.language != Language.OTHER) {
            InfoElement(
                name = stringResource(R.string.common_general_language),
                value = recipe.language.localizedName(resources),
                modifier = Modifier.padding(bottom = 12.dp),
            )
        }
        InfoElement(
            name = stringResource(R.string.common_general_visibility),
            value = when (recipe.visibility) {
                Visibility.PRIVATE -> stringResource(R.string.common_general_only_author)
                Visibility.SHARED -> stringResource(R.string.common_general_by_link)
                Visibility.PUBLIC -> stringResource(R.string.common_general_community)
            },
            modifier = Modifier.padding(bottom = 12.dp),
        )
        InfoElement(
            name = stringResource(R.string.common_general_creation_date),
            value = recipe.creationTimestamp.toLocalDate()
                .format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)),
            modifier = Modifier.padding(bottom = 12.dp),
        )
        if (recipe.updateTimestamp.toLocalDate() != recipe.creationTimestamp.toLocalDate()) {
            InfoElement(
                name = stringResource(R.string.common_general_update_date),
                value = recipe.updateTimestamp.toLocalDate()
                    .format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)),
                modifier = Modifier.padding(bottom = 12.dp),
            )
        }
        if (recipe.isOwned) {
            Divider(
                color = colors.backgroundSecondary,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp)
                    .height(1.dp)
            )
            Row(
                modifier = Modifier.padding(top = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                DynamicButton(
                    leftIcon = ImageVector.vectorResource(R.drawable.ic_edit),
                    iconsSize = 18.dp,
                    text = stringResource(R.string.common_general_edit),
                    unselectedForeground = colors.foregroundPrimary,
                    modifier = Modifier
                        .weight(1F)
                        .height(44.dp)
                        .fillMaxWidth(),
                    onClick = onEditRecipeClicked,
                )
                DynamicButton(
                    leftIcon = ImageVector.vectorResource(R.drawable.ic_trash),
                    iconsSize = 18.dp,
                    text = stringResource(R.string.common_general_delete),
                    isSelected = true,
                    modifier = Modifier
                        .weight(1F)
                        .height(44.dp)
                        .fillMaxWidth(),
                    onClick = onDeleteRecipeClicked,
                )
            }
        }
        Spacer(modifier = Modifier.navigationBarsPadding().height(32.dp))
    }
}
