package com.cactusknights.chefbook.ui.screens.recipeinput.dialogs

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.cactusknights.chefbook.R
import com.cactusknights.chefbook.domain.entities.common.Visibility
import com.cactusknights.chefbook.ui.screens.recipeinput.RecipeInputScreenViewModel
import com.cactusknights.chefbook.ui.screens.recipeinput.dialogs.elements.RadioElement
import com.cactusknights.chefbook.ui.screens.recipeinput.models.RecipeInputScreenEvent
import com.mysty.chefbook.core.ui.compose.providers.theme.LocalTheme

@Composable
fun VisibilityPickerDialog(
    viewModel: RecipeInputScreenViewModel,
) {
    val colors = LocalTheme.colors
    val typography = LocalTheme.typography

    val viewModelState = viewModel.state.collectAsState()
    val state = viewModelState.value.input.visibility

    Column(
        modifier = Modifier
            .padding(horizontal = 18.dp)
            .fillMaxWidth()
            .wrapContentHeight(),
    ) {
        Text(
            text = stringResource(R.string.common_general_visibility),
            maxLines = 1,
            style = typography.h4,
            color = colors.foregroundPrimary,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 18.dp)
        )
        Divider(
            color = colors.backgroundTertiary,
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
        )
        RadioElement(
            icon = ImageVector.vectorResource(R.drawable.ic_eye_closed),
            name = stringResource(R.string.common_general_only_author),
            description = stringResource(R.string.common_recipe_input_screen_private_description),
            isSelected = state == Visibility.PRIVATE,
            onSelected = {
                viewModel.obtainEvent(RecipeInputScreenEvent.SetVisibility(Visibility.PRIVATE))
            },
        )
        RadioElement(
            icon = ImageVector.vectorResource(R.drawable.ic_link),
            name = stringResource(R.string.common_general_by_link),
            description = stringResource(R.string.common_recipe_input_screen_shared_description),
            isSelected = state == Visibility.SHARED,
            onSelected = {
                viewModel.obtainEvent(RecipeInputScreenEvent.SetVisibility(Visibility.SHARED))
            },
        )
        RadioElement(
            icon = ImageVector.vectorResource(R.drawable.ic_earth),
            name = stringResource(R.string.common_general_community),
            description = stringResource(R.string.common_recipe_input_screen_public_description),
            isSelected = state == Visibility.PUBLIC,
            onSelected = {
                viewModel.obtainEvent(RecipeInputScreenEvent.SetVisibility(Visibility.PUBLIC))
            },
        )
        Spacer(modifier = Modifier.height(72.dp))
    }
}