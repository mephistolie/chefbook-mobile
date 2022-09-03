package com.cactusknights.chefbook.ui.screens.recipeinput.screens.ingredients.views.elements

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.cactusknights.chefbook.R
import com.mephistolie.compost.modifiers.simpleClickable
import com.cactusknights.chefbook.ui.themes.ChefBookTheme
import com.cactusknights.chefbook.ui.views.textfields.IndicatorTextField

@Composable
fun SectionField(
    name: String,
    onNameChange: (String) -> Unit,
    onDeleteClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = ChefBookTheme.colors
    val typography = ChefBookTheme.typography

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp)
            .padding(horizontal = 8.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(colors.backgroundPrimary)
            .padding(horizontal = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_drag_indicator),
            contentDescription = null,
            tint = colors.foregroundSecondary,
            modifier = Modifier
                .padding(top = 2.dp)
                .height(18.dp)
                .wrapContentWidth()
        )
        IndicatorTextField(
            value = name,
            modifier = Modifier
                .weight(1F)
                .fillMaxWidth(),
            onValueChange = onNameChange,
            textStyle = typography.headline1,
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next,
            ),
            label = {
                Text(
                    text = stringResource(R.string.common_general_section),
                    fontWeight = typography.headline1.fontWeight,
                    color = colors.foregroundPrimary
                )
            },
            maxLines = 1,
        )
        Icon(
            painter = painterResource(id = R.drawable.ic_cross),
            contentDescription = null,
            tint = colors.foregroundPrimary,
            modifier = Modifier
                .size(24.dp)
                .simpleClickable(onClick = onDeleteClick)
                .padding(2.dp)
        )
    }
}
