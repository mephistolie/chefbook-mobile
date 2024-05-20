package io.chefbook.features.recipebook.dashboard.ui.components.blocks

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridItemScope
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.mephistolie.compost.modifiers.simpleClickable
import io.chefbook.core.android.compose.providers.theme.LocalTheme
import io.chefbook.design.theme.dimens.DefaultIconSize
import io.chefbook.design.theme.shapes.RoundedCornerShape28Top
import io.chefbook.features.recipebook.core.ui.components.CategoryCard
import io.chefbook.features.recipebook.core.ui.components.CategoryCardSkeleton
import io.chefbook.features.recipebook.dashboard.ui.components.elements.NewCategoryCard
import io.chefbook.sdk.category.api.external.domain.entities.Category
import kotlin.math.min
import io.chefbook.core.android.R as coreR
import io.chefbook.design.R as designR

private const val KEY_PREFIX = "category_card"

internal fun LazyGridScope.categoriesBlock(
  categories: List<Category>?,
  drawDivider: Boolean,
  onCategoriesClick: () -> Unit,
  onCategoryClicked: (String) -> Unit,
) {
  if (categories?.isEmpty() == true) return

  item(
    span = { GridItemSpan(4) }
  ) {
    val colors = LocalTheme.colors
    val typography = LocalTheme.typography

    Row(
      modifier = Modifier
        .let {
          if (drawDivider) {
            it
              .background(colors.divider)
              .background(colors.backgroundPrimary, RoundedCornerShape28Top)
          } else it
        }
        .padding(20.dp, if (drawDivider) 20.dp else 32.dp, 20.dp, 16.dp)
        .simpleClickable(debounceInterval = 1000L, onCategoriesClick),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically,
    ) {
      Text(
        text = stringResource(id = coreR.string.common_general_categories),
        style = typography.h2,
        color = colors.foregroundPrimary,
      )
      Icon(
        painter = painterResource(designR.drawable.ic_arrow_circle_right),
        contentDescription = null,
        modifier = Modifier.size(DefaultIconSize),
        tint = colors.foregroundPrimary,
      )
    }
  }
  if (categories != null) {
    items(
      count = categories.size,
      key = { index -> "${KEY_PREFIX}_${categories[index].id}" }
    ) { index ->
      CategoryCard(
        id = categories[index].id,
        name = categories[index].name,
        emoji = categories[index].emoji,
        modifier = categoryCardModifier(index),
      ) { onCategoryClicked(it) }
    }
  } else {
    items(4) { index ->
      CategoryCardSkeleton(modifier = categoryCardModifier(index))
    }
  }
}

@OptIn(ExperimentalFoundationApi::class)
private fun LazyGridItemScope.categoryCardModifier(index: Int): Modifier {
  val position = index % 4
  return Modifier
    .padding(
      start = when (position) {
        1 -> 8.dp
        2 -> 4.dp
        3 -> 0.dp
        else -> 12.dp
      },
      end = when (position) {
        1 -> 4.dp
        2 -> 8.dp
        3 -> 12.dp
        else -> 0.dp
      }
    )
    .animateItemPlacement()
}
