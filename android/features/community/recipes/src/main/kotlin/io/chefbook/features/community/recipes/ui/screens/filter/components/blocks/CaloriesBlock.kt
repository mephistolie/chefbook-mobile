package io.chefbook.features.community.recipes.ui.screens.filter.components.blocks

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.core.text.isDigitsOnly
import com.mephistolie.compost.modifiers.clippedBackground
import com.mephistolie.compost.modifiers.padding
import io.chefbook.core.android.compose.providers.theme.LocalTheme
import io.chefbook.design.components.textfields.OutlinedTextField
import io.chefbook.design.theme.shapes.RoundedCornerShape28
import io.chefbook.core.android.R as coreR

internal fun LazyListScope.caloriesBlock(
  minCalories: Int?,
  maxCalories: Int?,
  onMinCaloriesChange: (Int?) -> Unit,
  onMaxCaloriesChange: (Int?) -> Unit,
  modifier: Modifier = Modifier,
) {
  item {
    val colors = LocalTheme.colors
    val typography = LocalTheme.typography

    Column(
      modifier = modifier
        .padding(bottom = 8.dp)
        .background(colors.backgroundPrimary, RoundedCornerShape28)
        .fillMaxWidth()
        .padding(12.dp)
    ) {
      Text(
        text = stringResource(coreR.string.common_general_calories),
        style = typography.h2,
        color = colors.foregroundPrimary,
        modifier = Modifier
          .padding(horizontal = 4.dp, top = 4.dp)
          .wrapContentHeight()
      )
      Text(
        text = stringResource(coreR.string.common_general_in_100_g),
        style = typography.body2,
        color = colors.foregroundSecondary,
        modifier = Modifier
          .padding(horizontal = 4.dp, bottom = 16.dp)
          .wrapContentHeight()
      )
      Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        OutlinedTextField(
          value = minCalories?.toString().orEmpty(),
          onValueChange = { text -> if (text.isDigitsOnly()) onMinCaloriesChange(text.toIntOrNull()) },
          keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number,
            imeAction = ImeAction.Done
          ),
          modifier = modifier
            .weight(1F)
            .fillMaxWidth(),
          hint = "От",
        )
        Text(
          text = "-",
          style = typography.h1,
          color = colors.foregroundPrimary,
        )
        OutlinedTextField(
          value = maxCalories?.toString().orEmpty(),
          onValueChange = { text -> if (text.isDigitsOnly()) onMaxCaloriesChange(text.toIntOrNull()) },
          keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number,
            imeAction = ImeAction.Done
          ),
          modifier = modifier
            .weight(1F)
            .fillMaxWidth(),
          hint = "До",
        )
      }
    }
  }
}
