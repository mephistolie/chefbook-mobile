package com.mysty.chefbook.features.recipe.info.ui.components.cooking

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mysty.chefbook.api.recipe.domain.entities.cooking.CookingItem
import com.mysty.chefbook.core.android.compose.providers.theme.LocalTheme
import com.mysty.chefbook.design.components.dividers.Divider
import com.mysty.chefbook.features.recipe.info.ui.components.common.Section

@Composable
internal fun CookingPage(
  cooking: List<CookingItem>,
  time: Int?,
  onStepPictureClicked: (String) -> Unit,
) {
  val colors = LocalTheme.colors
  val stepIndexes = remember {
    val map = mutableMapOf<String, Int>()
    var stepNumber = 1
    cooking.forEach { item ->
      if (item is CookingItem.Step) {
        map[item.id] = stepNumber
        stepNumber += 1
      }
    }
    map
  }

  LazyColumn(
    modifier = Modifier
      .padding(horizontal = 12.dp)
      .fillMaxWidth()
      .wrapContentHeight()
  ) {
    item {
      Spacer(modifier = Modifier.height(12.dp))
    }
    if (time != null) {
      item { ServingsBlock(time) }
    }
    items(
      count = cooking.size,
      key = { index -> cooking[index].id }
    ) { index ->
      when (val item = cooking[index]) {
        is CookingItem.Section -> {
          Section(
            name = item.name,
            modifier = Modifier.padding(bottom = 12.dp),
          )
        }
        is CookingItem.Step -> {
          CookingStep(
            number = stepIndexes[item.id] ?: 1,
            step = item,
            modifier = Modifier.padding(bottom = 4.dp),
            onPictureClicked = onStepPictureClicked,
          )
          if (index + 1 < cooking.size && cooking[index + 1] is CookingItem.Section) {
            Divider(
              color = colors.backgroundSecondary,
              modifier = Modifier
                .padding(vertical = 8.dp)
                .fillMaxWidth()
                .height(1.dp)
            )
          }
        }
        else -> Unit
      }
    }
    item {
      Spacer(
        modifier = Modifier
          .navigationBarsPadding()
          .height(32.dp)
      )
    }
  }
}
