package io.chefbook.features.shoppinglist.control.ui.screen.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.mephistolie.compost.modifiers.clippedBackground
import io.chefbook.core.android.compose.providers.theme.LocalTheme
import io.chefbook.design.components.buttons.DynamicButton
import io.chefbook.design.theme.shapes.BottomSheetShape
import io.chefbook.core.android.R as coreR
import io.chefbook.design.R as designR

internal val shoppingListActionBarHeight = 80.dp

@Composable
internal fun ShoppingListActionBar(
  onAddPurchaseClick: () -> Unit,
  onDoneClick: () -> Unit,
  isDoneButtonActive: Boolean,
  modifier: Modifier = Modifier,
) {
  val colors = LocalTheme.colors

  Row(
    modifier = modifier
      .background(colors.backgroundSecondary, BottomSheetShape)
      .padding(top = 1.dp)
      .clippedBackground(colors.backgroundPrimary, BottomSheetShape)
      .navigationBarsPadding()
      .fillMaxWidth()
      .height(shoppingListActionBarHeight)
      .padding(12.dp)
  ) {
    DynamicButton(
      text = stringResource(coreR.string.common_general_add),
      leftIcon = ImageVector.vectorResource(id = designR.drawable.ic_add),
      onClick = onAddPurchaseClick,
      isSelected = false,
      unselectedForeground = colors.foregroundPrimary,
      modifier = Modifier
        .weight(1F)
        .padding(end = 4.dp)
        .fillMaxSize(),
    )
    DynamicButton(
      leftIcon = ImageVector.vectorResource(id = designR.drawable.ic_check),
      onClick = onDoneClick,
      isSelected = isDoneButtonActive,
      isEnabled = isDoneButtonActive,
      modifier = Modifier
        .padding(start = 4.dp)
        .width(56.dp)
        .fillMaxHeight(),
    )
  }
}
