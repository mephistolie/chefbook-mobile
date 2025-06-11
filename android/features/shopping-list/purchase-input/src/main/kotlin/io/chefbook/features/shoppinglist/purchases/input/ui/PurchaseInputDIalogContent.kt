package io.chefbook.features.shoppinglist.purchases.input.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.mephistolie.compost.modifiers.clippedBackground
import io.chefbook.core.compose.providers.theme.LocalTheme
import io.chefbook.design.components.buttons.IconButton
import io.chefbook.design.components.buttons.DynamicButton
import io.chefbook.design.theme.shapes.SmoothCornerShape28Top
import io.chefbook.features.shoppinglist.purchases.input.ui.mvi.PurchaseInputDialogIntent
import io.chefbook.libs.models.measureunit.standardUnits
import io.chefbook.sdk.shoppinglist.api.external.domain.entities.Purchase
import io.chefbook.ui.common.extensions.localizedName
import io.chefbook.ui.common.extensions.stringToMeasureUnit
import io.chefbook.core.res as CoreR
import io.chefbook.design.R as designR

@OptIn(ExperimentalComposeUiApi::class, ExperimentalLayoutApi::class)
@Composable
internal fun PurchaseInputDialogContent(
  state: Purchase,
  onIntent: (PurchaseInputDialogIntent) -> Unit,
) {
  val keyboardController = LocalSoftwareKeyboardController.current
  val resources = LocalContext.current.resources
  val focusRequester = remember { FocusRequester() }

  val colors = LocalTheme.colors
  val typography = LocalTheme.typography

  Column(
    modifier = Modifier
      .imePadding()
      .clippedBackground(colors.backgroundPrimary, SmoothCornerShape28Top)
      .padding(horizontal = 18.dp)
      .fillMaxWidth()
      .wrapContentHeight(),
  ) {
    Box(
      contentAlignment = Alignment.TopEnd
    ) {
      Text(
        text = stringResource(CoreR.string.common_general_purchase),
        maxLines = 1,
        style = typography.h4,
        color = colors.foregroundPrimary,
        textAlign = TextAlign.Center,
        modifier = Modifier
          .fillMaxWidth()
          .padding(vertical = 18.dp)
      )
      IconButton(
        icon = ImageVector.vectorResource(designR.drawable.ic_cross),
        onClick = {
          keyboardController?.hide()
          onIntent(PurchaseInputDialogIntent.Close)
        },
        modifier = Modifier
          .padding(top = 18.dp)
          .size(28.dp),
        colors = ButtonDefaults.buttonColors(backgroundColor = colors.foregroundPrimary.copy(alpha = 0.25F)),
        tint = Color.White
      )
    }
    Divider(
      color = colors.backgroundSecondary,
      modifier = Modifier
        .fillMaxWidth()
        .height(1.dp)
    )
    ThemedIndicatorTextField(
      value = state.name,
      modifier = Modifier
        .focusRequester(focusRequester)
        .fillMaxWidth(),
      onValueChange = { name -> onIntent(PurchaseInputDialogIntent.SetName(name)) },
      keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
      label = {
        Text(
          stringResource(CoreR.string.common_general_name),
          color = colors.foregroundPrimary
        )
      },
    )
    var amount = if (state.amount != null) state.amount.toString() else ""
    amount = if (amount.lastOrNull() == '0') amount.substring(0, amount.lastIndex) else amount
    ThemedIndicatorTextField(
      value = amount,
      modifier = Modifier.fillMaxWidth(),
      onValueChange = { amount ->
        onIntent(PurchaseInputDialogIntent.SetAmount(amount.toFloatOrNull()))
      },
      keyboardOptions = KeyboardOptions(
        keyboardType = KeyboardType.Decimal,
        imeAction = ImeAction.Next
      ),
      label = {
        Text(
          stringResource(CoreR.string.common_general_amount),
          color = colors.foregroundPrimary
        )
      },
    )
    ThemedIndicatorTextField(
      value = state.measureUnit?.localizedName(resources) ?: "",
      modifier = Modifier.fillMaxWidth(),
      onValueChange = { unit ->
        onIntent(PurchaseInputDialogIntent.SetMeasureUnit(stringToMeasureUnit(unit, resources)))
      },
      keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
      keyboardActions = KeyboardActions { onIntent(PurchaseInputDialogIntent.Close) },
      label = {
        Text(
          stringResource(CoreR.string.common_general_unit),
          color = colors.foregroundPrimary
        )
      },
    )
    FlowRow(
      modifier = Modifier
        .wrapContentWidth()
        .padding(top = 8.dp),
      horizontalArrangement = Arrangement.spacedBy(6.dp),
      verticalArrangement = Arrangement.spacedBy(6.dp),
    ) {
      for (unit in standardUnits) {
        DynamicButton(
          text = unit.localizedName(resources),
          onClick = {
            onIntent(PurchaseInputDialogIntent.SetMeasureUnit(if (state.measureUnit != unit) unit else null))
          },
          modifier = Modifier
            .height(32.dp)
            .widthIn(min = 36.dp),
          textStyle = typography.headline2,
          horizontalPadding = 8.dp,
          selectedBackground = colors.foregroundPrimary,
          isSelected = state.measureUnit == unit,
        )
      }
    }
    Spacer(modifier = Modifier.height(12.dp))

    LaunchedEffect(Unit) {
      focusRequester.requestFocus()
    }
  }
}
