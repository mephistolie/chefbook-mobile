package com.cactusknights.chefbook

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.cactusknights.chefbook.legacy.ui.Tab

@Preview
@Composable
fun BottomNavigationBar() {

    val selectedRoute = Tab.Recipes.route

    val items = listOf(Tab.ShoppingList, Tab.Recipes)

    val navShape = RectangleShape

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(navShape)
            .background(colorResource(R.color.app_background)),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {

        items.forEach {
            val isSelected = it.route == selectedRoute

            val color = if (isSelected)
                colorResource(R.color.deep_orange_light)
            else
                MaterialTheme.colors.onSurface.copy(alpha = 0.7F)
            IconButton(onClick = { /*TODO*/ }) {
                Stroke
                Icon(
                    painter = painterResource(id = it.iconId),
                    contentDescription = null,
                    tint = color
                )
            }
        }
    }
}