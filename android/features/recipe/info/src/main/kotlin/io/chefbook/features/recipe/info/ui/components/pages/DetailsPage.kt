package io.chefbook.features.recipe.info.ui.components.pages

import android.annotation.SuppressLint
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
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import io.chefbook.core.android.compose.providers.theme.LocalTheme
import io.chefbook.design.components.buttons.DynamicButton
import io.chefbook.features.recipe.info.ui.components.details.categories.CategoriesBlock
import io.chefbook.features.recipe.info.ui.components.details.diet.DietWidget
import io.chefbook.features.recipe.info.ui.components.details.info.InfoElement
import io.chefbook.features.recipe.info.ui.mvi.RecipeScreenState
import io.chefbook.libs.models.language.Language
import io.chefbook.libs.utils.time.parseTimestampSafely
import io.chefbook.sdk.recipe.core.api.external.domain.entities.RecipeMeta.Visibility
import io.chefbook.ui.common.extensions.localizedName
import kotlinx.datetime.toJavaLocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import io.chefbook.core.android.R as coreR
import io.chefbook.design.R as designR

@SuppressLint("NewApi")
@Composable
internal fun DetailsPage(
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

  val creationTimestamp = remember(recipe.creationTimestamp) {
    parseTimestampSafely(recipe.creationTimestamp)?.toJavaLocalDateTime()
      ?.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM))
  }
  val updateTimestamp = remember(recipe.updateTimestamp) {
    parseTimestampSafely(recipe.updateTimestamp)?.toJavaLocalDateTime()
      ?.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM))
  }

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
    if (recipe.hasDietData) {
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
        name = stringResource(coreR.string.common_general_description),
        value = description,
        modifier = Modifier.padding(bottom = 12.dp),
      )
    }
    recipe.owner.name?.let { author ->
      InfoElement(
        name = stringResource(coreR.string.common_general_author),
        value = author,
        modifier = Modifier.padding(bottom = 12.dp),
      )
    }
    if (recipe.language != Language.OTHER) {
      InfoElement(
        name = stringResource(coreR.string.common_general_language),
        value = recipe.language.localizedName(resources),
        modifier = Modifier.padding(bottom = 12.dp),
      )
    }
    InfoElement(
      name = stringResource(coreR.string.common_general_visibility),
      value = when (recipe.visibility) {
        Visibility.PRIVATE -> stringResource(coreR.string.common_general_only_author)
        Visibility.LINK -> stringResource(coreR.string.common_general_by_link)
        Visibility.PUBLIC -> stringResource(coreR.string.common_general_community)
      },
      modifier = Modifier.padding(bottom = 12.dp),
    )
    creationTimestamp?.let {
      InfoElement(
        name = stringResource(coreR.string.common_general_creation_date),
        value = creationTimestamp,
        modifier = Modifier.padding(bottom = 12.dp),
      )
    }
    if (updateTimestamp != creationTimestamp && updateTimestamp != null) {
      InfoElement(
        name = stringResource(coreR.string.common_general_update_date),
        value = updateTimestamp,
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
          leftIcon = ImageVector.vectorResource(designR.drawable.ic_edit),
          iconsSize = 18.dp,
          text = stringResource(coreR.string.common_general_edit),
          unselectedForeground = colors.foregroundPrimary,
          modifier = Modifier
            .weight(1F)
            .height(44.dp)
            .fillMaxWidth(),
          onClick = onEditRecipeClicked,
        )
        DynamicButton(
          leftIcon = ImageVector.vectorResource(designR.drawable.ic_trash),
          iconsSize = 18.dp,
          text = stringResource(coreR.string.common_general_delete),
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
