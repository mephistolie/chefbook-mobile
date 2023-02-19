package com.mysty.chefbook.features.recipe.info.ui.components.common.content.loaded

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.mysty.chefbook.design.components.buttons.DynamicButton
import com.mysty.chefbook.features.recipe.info.R

@Composable
fun BoxScope.AddToShoppingListFab(
  isVisible: Boolean,
  onClick: () -> Unit,
) {
  AnimatedVisibility(
    visible = isVisible,
    modifier = Modifier
      .align(Alignment.BottomCenter)
      .wrapContentWidth()
      .navigationBarsPadding()
      .padding(bottom = 16.dp),
    enter = fadeIn(),
    exit = fadeOut(),
  ) {
    DynamicButton(
      leftIcon = ImageVector.vectorResource(R.drawable.ic_add),
      text = stringResource(R.string.common_recipe_screen_add_to_shopping_list).uppercase(),
      isSelected = true,
      modifier = Modifier.height(48.dp),
      onClick = onClick,
    )
  }
}