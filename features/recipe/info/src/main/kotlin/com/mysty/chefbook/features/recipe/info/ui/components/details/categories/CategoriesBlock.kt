package com.mysty.chefbook.features.recipe.info.ui.components.blocks

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.google.accompanist.flowlayout.FlowRow
import com.mephistolie.compost.modifiers.simpleClickable
import com.mysty.chefbook.api.category.domain.entities.Category
import com.mysty.chefbook.core.android.compose.providers.theme.LocalTheme
import com.mysty.chefbook.design.components.buttons.DynamicButton
import com.mysty.chefbook.features.recipe.info.R

@Composable
internal fun CategoriesBlock(
  categories: List<Category>,
  onChangeCategoriesButtonClicked: () -> Unit,
  onCategoryButtonClicked: (String) -> Unit,
) {
  val colors = LocalTheme.colors
  val typography = LocalTheme.typography

  Column {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .simpleClickable(onClick = onChangeCategoriesButtonClicked)
        .padding(top = 8.dp, bottom = 4.dp),
      verticalAlignment = Alignment.CenterVertically,
    ) {
      Text(
        text = stringResource(R.string.common_general_categories),
        style = typography.headline1,
        color = colors.foregroundSecondary,
      )
      Icon(
        imageVector = ImageVector.vectorResource(R.drawable.ic_edit),
        tint = colors.foregroundSecondary,
        modifier = Modifier
          .padding(start = 2.dp, top = 2.dp)
          .size(12.dp)
          .aspectRatio(1F),
        contentDescription = null,
      )
    }
    FlowRow {
      for (category in categories) {
        DynamicButton(
          text = "${category.cover.orEmpty()} ${category.name}".trim(),
          onClick = { onCategoryButtonClicked(category.id) },
          modifier = Modifier
            .padding(top = 8.dp, end = 8.dp)
            .height(38.dp),
          horizontalPadding = 8.dp,
          unselectedForeground = colors.foregroundPrimary,
        )
      }
    }
    Divider(
      color = colors.backgroundSecondary,
      modifier = Modifier
        .fillMaxWidth()
        .padding(top = 16.dp)
        .height(1.dp)
    )
  }
}