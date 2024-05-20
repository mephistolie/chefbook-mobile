package io.chefbook.features.community.recipes.ui.screens.filter.components.blocks

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.mephistolie.compost.modifiers.clippedBackground
import com.mephistolie.compost.modifiers.padding
import io.chefbook.core.android.compose.providers.theme.LocalTheme
import io.chefbook.design.components.buttons.DynamicButton
import io.chefbook.design.theme.shapes.RoundedCornerShape28
import io.chefbook.features.community.recipes.R

@OptIn(ExperimentalLayoutApi::class)
internal fun LazyListScope.specificitiesBlock(
  highRatingOnly: Boolean,
  onHighRatingOnlyClick: () -> Unit,
  modifier: Modifier = Modifier,
) {
  item {
    val colors = LocalTheme.colors
    val typography = LocalTheme.typography

    Column(
      modifier = modifier
        .padding(bottom = 8.dp)
        .clippedBackground(colors.backgroundPrimary, RoundedCornerShape28)
        .fillMaxWidth()
        .padding(12.dp)
    ) {
      Text(
        text = stringResource(R.string.common_community_recipes_filter_screen_specificities),
        style = typography.h2,
        color = colors.foregroundPrimary,
        modifier = Modifier
          .padding(horizontal = 4.dp, top = 4.dp, bottom = 16.dp)
          .wrapContentHeight()
      )
      FlowRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
      ) {
        DynamicButton(
          text = "\uD83D\uDE0B ${stringResource(R.string.common_community_recipes_filter_screen_high_rating)}",
          textStyle = typography.body1,
          iconsSize = 18.dp,
          horizontalPadding = 16.dp,
          selectedBackground = colors.foregroundPrimary,
          unselectedForeground = colors.foregroundPrimary,
          isSelected = highRatingOnly,
          onClick = onHighRatingOnlyClick,
          modifier = Modifier.height(42.dp),
        )
      }
    }
  }
}
