package io.chefbook.features.recipe.input.ui.screens.details.components

import android.app.TimePickerDialog
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import io.chefbook.core.android.compose.providers.theme.LocalTheme
import io.chefbook.core.android.utils.minutesToTimeString
import io.chefbook.design.components.buttons.DynamicButton
import io.chefbook.design.theme.dimens.ButtonSmallHeight
import io.chefbook.sdk.recipe.crud.api.external.domain.entities.RecipeInput
import io.chefbook.core.android.R as coreR

@Composable
fun TimeBlock(
  state: RecipeInput,
  onTimeInput: (Int, Int) -> Unit,
  modifier: Modifier = Modifier,
) {
  val context = LocalContext.current
  val resources = context.resources

  val colors = LocalTheme.colors
  val typography = LocalTheme.typography

  val time = state.time

  Row(
    modifier = modifier,
    horizontalArrangement = Arrangement.SpaceBetween,
    verticalAlignment = Alignment.CenterVertically,
  ) {
    Text(
      text = stringResource(coreR.string.common_general_time),
      style = typography.headline1,
      color = colors.foregroundPrimary,
    )
    DynamicButton(
      text = when (time) {
        null -> stringResource(coreR.string.common_general_specify)
        else -> minutesToTimeString(time, resources)
      },
      cornerRadius = 12.dp,
      unselectedForeground = colors.foregroundPrimary,
      onClick = {
        TimePickerDialog(
          context,
          { _, hourOfDay, minute -> onTimeInput(hourOfDay, minute) },
          if (time != null && time > 0) time / 60 else 0,
          if (time != null && time > 0) time % 60 else 15,
          true,
        ).show()
      },
      modifier = Modifier
        .requiredWidth(128.dp)
        .height(ButtonSmallHeight),
    )
  }
}
