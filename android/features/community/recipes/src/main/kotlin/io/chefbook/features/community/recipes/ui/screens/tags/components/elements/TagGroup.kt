package io.chefbook.features.community.recipes.ui.screens.tags.components.elements

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mephistolie.compost.modifiers.padding
import io.chefbook.core.android.compose.providers.theme.LocalTheme
import io.chefbook.design.theme.shapes.RoundedCornerShape28
import io.chefbook.features.community.recipes.ui.screens.filter.components.elements.TagButton
import io.chefbook.features.community.recipes.ui.mvi.FilterState

private const val SPAN_SIZE = 4

@Composable
internal fun TagGroup(
  group: FilterState.TagGroup,
  selectedTags: List<String>,
  onTagSelected: (String) -> Unit,
  onTagUnselected: (String) -> Unit,
  modifier: Modifier = Modifier,
) {
  val colors = LocalTheme.colors
  val typography = LocalTheme.typography

  Column(
    modifier = modifier
      .padding(bottom = 8.dp)
      .background(colors.backgroundPrimary, RoundedCornerShape28)
      .padding(12.dp, 12.dp, 12.dp, 24.dp)
  ) {
    Text(
      text = group.name.orEmpty(),
      style = typography.h2,
      color = colors.foregroundPrimary,
      modifier = Modifier
        .padding(horizontal = 4.dp, top = 4.dp, bottom = 16.dp)
        .wrapContentHeight()
    )
    val tagChunks = group.tags.chunked(SPAN_SIZE)
    tagChunks.forEach { tags ->
      Row {
        repeat(SPAN_SIZE) { i ->
          val tag = tags.getOrNull(i)
          if (tag != null) {
            TagButton(
              tag = tags[i],
              isSelected = tags[i].id in selectedTags,
              onSelected = onTagSelected,
              onUnselected = onTagUnselected,
              modifier = Modifier
                .weight(1F)
                .padding(
                  vertical = 4.dp,
                  start = if (i == 0) 0.dp else 4.dp,
                  end = if (i + 1 == SPAN_SIZE) 0.dp else 4.dp,
                ),
            )
          } else {
            Spacer(modifier = Modifier.weight(1F))
          }
        }
      }
    }
  }
}
