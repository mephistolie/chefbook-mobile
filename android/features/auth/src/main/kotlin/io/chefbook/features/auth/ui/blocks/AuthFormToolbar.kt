package io.chefbook.features.auth.form.ui.blocks

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.mephistolie.compost.modifiers.simpleClickable
import io.chefbook.core.android.compose.providers.theme.LocalTheme
import io.chefbook.design.R
import io.chefbook.design.theme.dimens.DefaultIconSize

@Composable
fun AuthFormToolbar(
  login: String?,
  onBackClick: () -> Unit,
) {
  val colors = LocalTheme.colors
  val typography = LocalTheme.typography

  Box(
    modifier = Modifier
      .padding(top = 20.dp)
      .fillMaxWidth(),
    contentAlignment = Alignment.CenterStart,
  ) {
    Icon(
      imageVector = ImageVector.vectorResource(R.drawable.ic_arrow_left),
      tint = colors.foregroundPrimary,
      contentDescription = null,
      modifier = Modifier
        .size(DefaultIconSize)
        .simpleClickable(onClick = onBackClick),
    )
    login?.let {
      Text(
        text = login,
        style = typography.headline1,
        textAlign = TextAlign.Center,
        color = colors.foregroundPrimary,
        overflow = TextOverflow.Ellipsis,
        maxLines = 1,
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = DefaultIconSize),
      )
    }
  }
}
