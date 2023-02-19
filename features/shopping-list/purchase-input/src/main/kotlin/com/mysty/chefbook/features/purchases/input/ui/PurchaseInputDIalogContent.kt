package com.mysty.chefbook.features.purchases.input.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.google.accompanist.flowlayout.FlowRow
import com.mysty.chefbook.api.common.entities.unit.standardUnits
import com.mysty.chefbook.api.shoppinglist.domain.entities.Purchase
import com.mysty.chefbook.core.android.compose.providers.theme.LocalTheme
import com.mysty.chefbook.core.constants.Strings
import com.mysty.chefbook.design.components.buttons.CircleIconButton
import com.mysty.chefbook.design.components.buttons.DynamicButton
import com.mysty.chefbook.design.components.textfields.ThemedIndicatorTextField
import com.mysty.chefbook.features.purchases.input.ui.mvi.PurchaseInputDialogIntent
import com.mysty.chefbook.features.shoppinglist.purchaseinput.R
import com.mysty.chefbook.ui.common.extensions.localizedName
import com.mysty.chefbook.ui.common.extensions.stringToMeasureUnit


@OptIn(ExperimentalComposeUiApi::class)
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
      .padding(horizontal = 18.dp)
      .fillMaxWidth()
      .wrapContentHeight(),
  ) {
    Box(
      contentAlignment = Alignment.TopEnd
    ) {
      Text(
        text = stringResource(R.string.common_general_purchase),
        maxLines = 1,
        style = typography.h4,
        color = colors.foregroundPrimary,
        textAlign = TextAlign.Center,
        modifier = Modifier
          .fillMaxWidth()
          .padding(vertical = 18.dp)
      )
      CircleIconButton(
        iconId = R.drawable.ic_cross,
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
          stringResource(R.string.common_general_name),
          color = colors.foregroundPrimary
        )
      },
    )
    ThemedIndicatorTextField(
      value = if (state.amount != null) state.amount.toString() else Strings.EMPTY,
      modifier = Modifier.fillMaxWidth(),
      onValueChange = { amount ->
        onIntent(PurchaseInputDialogIntent.SetAmount(amount.toIntOrNull() ?: 0))
      },
      keyboardOptions = KeyboardOptions(
        keyboardType = KeyboardType.Decimal,
        imeAction = ImeAction.Next
      ),
      label = {
        Text(
          stringResource(R.string.common_general_amount),
          color = colors.foregroundPrimary
        )
      },
    )
    ThemedIndicatorTextField(
      value = state.unit?.localizedName(resources) ?: Strings.EMPTY,
      modifier = Modifier.fillMaxWidth(),
      onValueChange = { unit ->
        onIntent(PurchaseInputDialogIntent.SetMeasureUnit(stringToMeasureUnit(unit, resources)))
      },
      keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
      keyboardActions = KeyboardActions { onIntent(PurchaseInputDialogIntent.Close) },
      label = {
        Text(
          stringResource(R.string.common_general_unit),
          color = colors.foregroundPrimary
        )
      },
    )
    FlowRow(
      modifier = Modifier
        .wrapContentWidth()
        .padding(top = 8.dp),
      mainAxisSpacing = 8.dp,
      crossAxisSpacing = 8.dp,
    ) {
      for (unit in standardUnits) {
        DynamicButton(
          text = unit.localizedName(resources),
          onClick = {
            onIntent(PurchaseInputDialogIntent.SetMeasureUnit(if (state.unit != unit) unit else null))
          },
          modifier = Modifier
            .height(32.dp)
            .widthIn(min = 36.dp),
          textStyle = typography.headline2,
          horizontalPadding = 8.dp,
          selectedBackground = colors.foregroundPrimary,
          isSelected = state.unit == unit,
        )
      }
    }
    Spacer(modifier = Modifier.height(12.dp))

    LaunchedEffect(Unit) {
      focusRequester.requestFocus()
    }
  }
}
