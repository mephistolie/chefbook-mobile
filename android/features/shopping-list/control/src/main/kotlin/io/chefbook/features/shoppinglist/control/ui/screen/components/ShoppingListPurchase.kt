package io.chefbook.features.shoppinglist.control.ui.screen.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.mephistolie.compost.modifiers.simpleClickable
import io.chefbook.core.android.compose.providers.theme.LocalTheme
import io.chefbook.design.components.checkboxes.Checkbox
import io.chefbook.design.theme.dimens.SmallIconSize
import io.chefbook.sdk.shoppinglist.api.external.domain.entities.Purchase
import io.chefbook.ui.common.extensions.localizedName
import io.chefbook.design.R as designR

@Composable
internal fun ShoppingListPurchase(
  purchase: Purchase,
  modifier: Modifier = Modifier,
  onEditClick: () -> Unit,
) {
  val colors = LocalTheme.colors
  val typography = LocalTheme.typography

  Row(
    verticalAlignment = Alignment.Top,
    horizontalArrangement = Arrangement.Start,
    modifier = modifier
      .fillMaxWidth()
      .wrapContentHeight(),
  ) {
    Checkbox(
      isChecked = purchase.isPurchased,
      onClick = {},
      isEnabled = false,
    )
    Spacer(modifier = Modifier.width(8.dp))
    Text(
      text = getFormattedText(purchase),
      style = typography.headline1,
      color = colors.foregroundPrimary,
    )
    Spacer(
      modifier = Modifier
        .weight(1F)
        .fillMaxWidth()
    )
    if (purchase.recipeId == null) {
      Icon(
        imageVector = ImageVector.vectorResource(id = designR.drawable.ic_edit),
        contentDescription = null,
        tint = colors.foregroundSecondary,
        modifier = Modifier
          .simpleClickable { onEditClick() }
          .size(SmallIconSize)
      )
    }
  }
}

@Composable
private fun getFormattedText(purchase: Purchase): AnnotatedString {
  val colors = LocalTheme.colors
  val typography = LocalTheme.typography
  val primaryStyle = SpanStyle(
    color = colors.foregroundPrimary,
    fontSize = typography.headline1.fontSize,
    fontWeight = typography.headline1.fontWeight,
    fontStyle = typography.headline1.fontStyle,
    fontFamily = typography.headline1.fontFamily,
  )
  val secondaryStyle = SpanStyle(
    color = colors.foregroundSecondary,
    fontSize = typography.headline2.fontSize,
    fontWeight = typography.headline2.fontWeight,
    fontStyle = typography.headline2.fontStyle,
    fontFamily = LocalTheme.typography.headline2.fontFamily,
  )

  return buildAnnotatedString {
    withStyle(primaryStyle) {
      append(purchase.name)
    }
    withStyle(secondaryStyle) {
      purchase.amount?.let { append(" $it") }
      purchase.measureUnit?.let { append(" ${it.localizedName(LocalContext.current.resources)}") }
      if (purchase.multiplier > 1) {
        append(" x${purchase.multiplier}")
      }
    }
  }
}
