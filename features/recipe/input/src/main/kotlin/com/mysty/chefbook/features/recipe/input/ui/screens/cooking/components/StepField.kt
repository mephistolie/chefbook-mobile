package com.mysty.chefbook.features.recipe.input.ui.screens.cooking.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.mephistolie.compost.modifiers.clippedBackground
import com.mephistolie.compost.modifiers.simpleClickable
import com.mysty.chefbook.api.recipe.domain.entities.cooking.CookingItem
import com.mysty.chefbook.core.android.compose.providers.theme.LocalTheme
import com.mysty.chefbook.design.components.buttons.DynamicButton
import com.mysty.chefbook.design.components.textfields.ThemedIndicatorTextField
import com.mysty.chefbook.design.theme.shapes.RoundedCornerShape16
import com.mysty.chefbook.features.recipe.input.R

@Composable
internal fun StepField(
    step: CookingItem.Step,
    number: Int,
    onDescriptionChange: (String) -> Unit,
    onAddPictureClick: () -> Unit,
    onDeletePictureClick: (Int) -> Unit,
    onDeleteClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = LocalTheme.colors
    val typography = LocalTheme.typography

    Column(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .clippedBackground(colors.backgroundPrimary, RoundedCornerShape16)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.Top,
        ) {
            Column(
                modifier = Modifier
                    .height(56.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                DynamicButton(
                    text = number.toString(),
                    textStyle = typography.subhead2,
                    modifier = Modifier
                        .size(18.dp),
                    onClick = {},
                    horizontalPadding = 0.dp,
                    isEnabled = false,
                )
                Icon(
                    painter = painterResource(id = R.drawable.ic_drag_indicator),
                    contentDescription = null,
                    tint = colors.foregroundSecondary,
                    modifier = Modifier
                        .padding(top = 6.dp)
                        .height(18.dp)
                        .wrapContentWidth()
                )
            }
            ThemedIndicatorTextField(
                value = step.description,
                modifier = Modifier
                    .weight(1F)
                    .fillMaxWidth(),
                onValueChange = onDescriptionChange,
                label = {
                    Text(
                        text = stringResource(R.string.common_general_step),
                        color = colors.foregroundPrimary
                    )
                },
            )
            Icon(
                painter = painterResource(id = R.drawable.ic_cross),
                contentDescription = null,
                tint = colors.foregroundPrimary,
                modifier = Modifier
                    .height(56.dp)
                    .width(24.dp)
                    .padding(vertical = 16.dp)
                    .simpleClickable(onClick = onDeleteClick)
                    .padding(2.dp)
            )
        }
        LazyRow(
            modifier = Modifier
                .padding(top = 8.dp, bottom = 4.dp)
        ) {
            itemsIndexed(step.pictures.orEmpty()) { index, pictureUri ->
                StepPicture(
                    uri = pictureUri,
                    onDeleteClick = { onDeletePictureClick(index) },
                    modifier = Modifier
                        .padding(
                            start = if (index == 0) 36.dp else 0.dp,
                            end = 8.dp
                        ),
                )
            }
            item {
                StepAddPictureButton(
                    onAddPictureClick = onAddPictureClick,
                    modifier = Modifier
                        .padding(
                            start = if (step.pictures.isNullOrEmpty()) 36.dp else 0.dp,
                            end = 8.dp
                        )
                )
            }
        }
    }
}
