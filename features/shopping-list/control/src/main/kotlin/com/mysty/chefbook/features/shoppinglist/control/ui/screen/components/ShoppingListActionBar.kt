package com.mysty.chefbook.features.shoppinglist.control.ui.screen.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.mephistolie.compost.modifiers.clippedBackground
import com.mysty.chefbook.core.android.compose.providers.theme.LocalTheme
import com.mysty.chefbook.design.components.buttons.DynamicButton
import com.mysty.chefbook.design.theme.shapes.BottomSheetShape
import com.mysty.chefbook.features.shoppinglist.control.R

@Composable
fun ShoppingListActionBar(
    onAddPurchaseClick: () -> Unit,
    onDoneClick: () -> Unit,
    isDoneButtonActive: Boolean,
    modifier: Modifier = Modifier,
) {
    val colors = LocalTheme.colors

    Row(
        modifier = modifier
            .background(colors.backgroundSecondary)
            .padding(top = 6.dp)
            .fillMaxWidth()
            .wrapContentHeight()
            .clippedBackground(background = colors.backgroundPrimary, BottomSheetShape)
            .navigationBarsPadding()
            .padding(12.dp, 12.dp, 12.dp, 4.dp)
    ) {
        DynamicButton(
            text = stringResource(R.string.common_general_add),
            leftIcon = ImageVector.vectorResource(id = R.drawable.ic_add),
            onClick = onAddPurchaseClick,
            isSelected = false,
            cornerRadius = 16.dp,
            unselectedForeground = colors.foregroundPrimary,
            modifier = Modifier
                .weight(1F)
                .padding(end = 4.dp)
                .fillMaxWidth()
                .height(56.dp),
        )
        DynamicButton(
            leftIcon = ImageVector.vectorResource(id = R.drawable.ic_check),
            onClick = onDoneClick,
            isSelected = isDoneButtonActive,
            isEnabled = isDoneButtonActive,
            cornerRadius = 16.dp,
            modifier = Modifier
                .padding(start = 4.dp)
                .size(56.dp),
        )
    }
}
