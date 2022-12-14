package com.cactusknights.chefbook.ui.screens.recipebook.views.blocks

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.cactusknights.chefbook.R
import com.mephistolie.compost.modifiers.simpleClickable
import com.mysty.chefbook.core.ui.compose.providers.theme.LocalTheme
import com.mysty.chefbook.design.components.buttons.CircleIconButton
import com.mysty.chefbook.design.theme.shapes.RoundedCornerShape12

@Composable
fun TopBar(
    onCreateRecipeButtonClick: () -> Unit,
    onSearchFieldClick: () -> Unit,
    onFavouriteButtonClick: () -> Unit,
    modifier: Modifier = Modifier,
    scale: Float = 1F,
) {
    val colors = LocalTheme.colors
    val typography = LocalTheme.typography

    val barSize = 42.dp + (scale * 6).dp

    Row(
        modifier = modifier.wrapContentHeight(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_add),
            contentDescription = null,
            tint = colors.tintPrimary,
            modifier = Modifier
                .width(barSize - 6.dp)
                .simpleClickable(onClick = onCreateRecipeButtonClick)
                .padding(2.dp, 0.dp, 6.dp, 0.dp)
                .size(28.dp)
        )
        Button(
            onClick = { onSearchFieldClick() },
            modifier = Modifier
                .height(barSize)
                .fillMaxWidth()
                .weight(1f),
            colors = ButtonDefaults.buttonColors(backgroundColor = colors.backgroundSecondary),
            contentPadding = PaddingValues(0.dp),
            shape = RoundedCornerShape12,
            elevation = null,
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp, 0.dp),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_search),
                    contentDescription = null,
                    tint = colors.foregroundPrimary,
                    modifier = Modifier
                        .size(20.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = stringResource(id = R.string.common_general_search),
                    style = typography.headline1,
                    color = colors.foregroundSecondary,
                )
            }
        }
        CircleIconButton(
            iconId = R.drawable.ic_favourite,
            onClick = { onFavouriteButtonClick() },
            modifier = Modifier
                .padding(start = 8.dp)
                .size(barSize),
            contentPadding = 9.dp
        )
    }
}
