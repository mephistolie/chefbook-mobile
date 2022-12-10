package com.cactusknights.chefbook.ui.screens.shoppinglist.views

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.cactusknights.chefbook.ui.themes.ChefBookTheme
import com.cactusknights.chefbook.ui.views.buttons.DynamicButton
import com.mephistolie.compost.modifiers.clippedBackground

@Composable
fun ActionBar(
    onAddPurchaseClick: () -> Unit,
) {
    val colors = ChefBookTheme.colors

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .clippedBackground(background = colors.backgroundPrimary, RoundedCornerShape(28.dp, 28.dp, 0.dp, 0.dp))
            .navigationBarsPadding()
    ) {
        DynamicButton(
            text = "Добавить",
            onClick = onAddPurchaseClick,
            isSelected = false,
            cornerRadius = 16.dp,
            unselectedForeground = colors.foregroundPrimary,
            modifier = Modifier
                .padding(
                    start = 12.dp,
                    top = 12.dp,
                    end = 12.dp,
                    bottom = 4.dp,
                )
                .fillMaxWidth()
                .height(56.dp),
        )
    }
}
