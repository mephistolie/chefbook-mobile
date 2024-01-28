package io.chefbook.ui.common.components.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import io.chefbook.core.android.compose.providers.theme.LocalTheme
import io.chefbook.design.theme.colors.Gradients
import io.chefbook.core.android.R

@Composable
fun SubscriptionBadge(
  isPremium: Boolean,
  modifier: Modifier = Modifier,
) {
  val colors = LocalTheme.colors
  val typography = LocalTheme.typography

  Box(
    modifier = Modifier
      .background(
        brush = if (isPremium) Gradients.orangeBrush else Gradients.grayBrush(),
        shape = RoundedCornerShape(100)
      )
      .padding(vertical = 4.dp, horizontal = 8.dp)
      .defaultMinSize(minWidth = 48.dp),
    contentAlignment = Alignment.Center
  ) {
    Text(
      text = stringResource(
        if (isPremium) R.string.common_general_subscription_premium
        else R.string.common_general_subscription_free
      ),
      style = typography.subhead2,
      color = if (isPremium) Color.White else colors.foregroundPrimary,
    )
  }
}