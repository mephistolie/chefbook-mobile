package com.cactusknights.chefbook.ui.screens.shoppinglist.views

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.cactusknights.chefbook.R
import com.cactusknights.chefbook.ui.themes.ChefBookTheme
import com.cactusknights.chefbook.ui.views.buttons.DynamicButton
import com.mephistolie.compost.modifiers.clippedBackground

@Composable
fun ActionBar(
    onAddPurchaseClick: () -> Unit,
    onDoneClick: () -> Unit,
    isDoneButtonActive: Boolean,
    modifier: Modifier = Modifier,
) {
    val colors = ChefBookTheme.colors

    Row(
        modifier = Modifier
            .padding(top = 6.dp)
            .fillMaxWidth()
            .wrapContentHeight()
            .clippedBackground(background = colors.backgroundPrimary, RoundedCornerShape(28.dp, 28.dp, 0.dp, 0.dp))
            .navigationBarsPadding()
            .padding(12.dp, 12.dp, 12.dp, 4.dp)
    ) {
        DynamicButton(
            text = "Добавить",
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
