package io.chefbook.features.community.recipes.ui.screens.filter.components.blocks

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.mephistolie.compost.modifiers.padding
import io.chefbook.core.android.R
import io.chefbook.core.android.compose.providers.theme.LocalTheme
import io.chefbook.design.R as designR
import io.chefbook.design.theme.shapes.RoundedCornerShape28
import io.chefbook.features.community.recipes.ui.screens.filter.components.elements.TagButton
import io.chefbook.features.community.recipes.ui.mvi.FilterState
import io.chefbook.features.community.recipes.ui.screens.content.components.elements.FilterButton

private const val SPAN_SIZE = 4
private const val MAX_ROWS = 2

internal fun LazyListScope.tagGroupsBlocks(
  groups: List<FilterState.TagGroup>,
  selectedTags: List<String>,
  onTagSelected: (String) -> Unit,
  onTagUnselected: (String) -> Unit,
  onTagGroupExpandClicked: (String?) -> Unit,
  modifier: Modifier = Modifier,
) {
  items(groups) { group ->
    val colors = LocalTheme.colors
    val typography = LocalTheme.typography

    Column(
      modifier = modifier
        .padding(bottom = 8.dp)
        .background(colors.backgroundPrimary, RoundedCornerShape28)
        .padding(12.dp, 12.dp, 12.dp, 24.dp)
    ) {
      group.name?.let { name ->
        Text(
          text = name,
          style = typography.h2,
          color = colors.foregroundPrimary,
          modifier = Modifier
            .padding(horizontal = 4.dp, top = 4.dp, bottom = 16.dp)
            .wrapContentHeight()
        )
      }
      val tagChunks = group.tags.chunked(SPAN_SIZE)
      tagChunks.take(MAX_ROWS).forEachIndexed { row, tags ->
        Row {
          repeat(SPAN_SIZE) { i ->
            val cellModifier = Modifier
              .weight(1F)
              .padding(
                vertical = 4.dp,
                start = if (i == 0) 0.dp else 4.dp,
                end = if (i + 1 == SPAN_SIZE) 0.dp else 4.dp,
              )

            val tag = tags.getOrNull(i)
            val isLastCell = row + 1 == MAX_ROWS && i + 1 == SPAN_SIZE
            when {
              isLastCell && tagChunks.size > MAX_ROWS -> FilterButton(
                name = stringResource(R.string.common_general_more),
                modifier = cellModifier,
                tint = colors.foregroundSecondary,
                onClick = { onTagGroupExpandClicked(group.id) },
              ) {
                Icon(
                  imageVector = ImageVector.vectorResource(designR.drawable.ic_three_dots),
                  tint = colors.foregroundPrimary,
                  contentDescription = null,
                  modifier = Modifier.size(28.dp),
                )
              }
              tag != null -> TagButton(
                tag = tags[i],
                isSelected = tags[i].id in selectedTags,
                onSelected = onTagSelected,
                onUnselected = onTagUnselected,
                modifier = cellModifier,
              )
              else -> Spacer(modifier = Modifier.weight(1F))
            }
          }
        }
      }
    }
  }
}
