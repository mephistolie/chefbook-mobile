package io.chefbook.features.community.recipes.ui.screens.content.components.blocks

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import io.chefbook.core.android.compose.providers.theme.LocalTheme
import io.chefbook.core.android.R as coreR
import io.chefbook.design.R as designR
import io.chefbook.features.community.recipes.ui.screens.content.components.elements.FilterButton
import io.chefbook.sdk.tag.api.external.domain.entities.Tag

@Composable
internal fun FilterButtons(
  tags: List<Tag>,
  isChefMatchButtonVisible: Boolean,
  onSearchClick: () -> Unit,
  onFilterClick: () -> Unit,
  onTagClick: (Tag) -> Unit,
  onMoreTagsClick: () -> Unit,
  modifier: Modifier = Modifier,
) {
  val colors = LocalTheme.colors
  val typography = LocalTheme.typography

  LazyRow(
    modifier = modifier
      .fillMaxWidth()
      .padding(top = 12.dp, bottom = 20.dp),
    horizontalArrangement = Arrangement.spacedBy(8.dp),
  ) {
    item { Spacer(Modifier.width(4.dp)) }
    item {
      FilterButton(
        name = stringResource(coreR.string.common_general_search),
        onClick = onSearchClick,
      ) {
        Icon(
          imageVector = ImageVector.vectorResource(designR.drawable.ic_search),
          tint = colors.foregroundPrimary,
          contentDescription = null,
          modifier = Modifier.size(28.dp),
        )
      }
    }
    item {
      AnimatedVisibility(
        visible = isChefMatchButtonVisible,
        enter = fadeIn() + expandHorizontally(),
        exit = fadeOut() + shrinkHorizontally()
      ) {
        FilterButton(
          name = stringResource(coreR.string.common_general_chefmatch),
          onClick = {},
        ) {
          Icon(
            imageVector = ImageVector.vectorResource(designR.drawable.ic_favourite),
            tint = colors.foregroundPrimary,
            contentDescription = null,
            modifier = Modifier.size(28.dp),
          )
        }
      }
    }
    item {
      FilterButton(
        name = stringResource(coreR.string.common_general_filters),
        onClick = onFilterClick,
      ) {
        Icon(
          imageVector = ImageVector.vectorResource(designR.drawable.ic_slider),
          tint = colors.foregroundPrimary,
          contentDescription = null,
          modifier = Modifier.size(28.dp),
        )
      }
    }
    tags.forEach { tag ->
      item {
        FilterButton(
          name = tag.name,
          onClick = { onTagClick(tag) },
        ) {
          Text(
            text = tag.emoji.orEmpty(),
            style = typography.h1,
            color = colors.foregroundPrimary,
          )
        }
      }
    }
    item {
      FilterButton(
        name = stringResource(coreR.string.common_general_more),
        onClick = onMoreTagsClick,
      ) {
        Icon(
          imageVector = ImageVector.vectorResource(designR.drawable.ic_arrow_right),
          tint = colors.foregroundPrimary,
          contentDescription = null,
          modifier = Modifier.size(28.dp),
        )
      }
    }
    item { Spacer(Modifier.width(4.dp)) }
  }
}