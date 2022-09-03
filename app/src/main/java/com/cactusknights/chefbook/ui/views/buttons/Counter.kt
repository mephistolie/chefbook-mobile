package com.cactusknights.chefbook.ui.views.buttons

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cactusknights.chefbook.R
import com.cactusknights.chefbook.ui.themes.ChefBookTheme
import com.mephistolie.compost.modifiers.clippedBackground

@OptIn(ExperimentalMaterialApi::class, ExperimentalComposeUiApi::class)
@Composable
fun Counter(
    count: Int,
    onMinusClicked: () -> Unit,
    onPlusClicked: () -> Unit,
    onValueChange: (String) -> Unit = {},
    isTextEditable: Boolean = false,
    isMultiplier: Boolean = false,
) {
    val keyboardController = LocalSoftwareKeyboardController.current

    val colors = ChefBookTheme.colors
    val typography = ChefBookTheme.typography

    val text =
        if (count == 0) {
            ""
        } else {
            if (!isMultiplier) "$count" else "x${count}"
        }

    Row(
        modifier = Modifier
            .wrapContentWidth()
            .height(38.dp)
            .clippedBackground(colors.backgroundSecondary, RoundedCornerShape(12.dp))
    ) {
        IconButton(
            onClick = onMinusClicked,
            modifier = Modifier
                .size(36.dp),
            enabled = count > 1,
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.ic_remove),
                tint = if (count > 1) colors.foregroundPrimary else colors.foregroundSecondary,
                contentDescription = stringResource(id = R.string.common_general_email),
            )
        }
        Spacer(
            modifier = Modifier
                .width(2.dp)
                .fillMaxHeight()
                .background(colors.backgroundTertiary)
        )
        Box(
            modifier = Modifier
                .width(52.dp)
                .height(36.dp)
                .background(colors.backgroundSecondary),
            contentAlignment = Alignment.Center,
        ) {
            BasicTextField(
                value = text,
                onValueChange = onValueChange,
                modifier = Modifier
                    .clip(RectangleShape),
                maxLines = 1,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        keyboardController?.hide()
                    }
                ),
                readOnly = !isTextEditable,
                textStyle = typography.headline1.copy(
                    color = colors.foregroundPrimary,
                    textAlign = TextAlign.Center,
                ),
            )
        }
        Spacer(
            modifier = Modifier
                .width(2.dp)
                .fillMaxHeight()
                .background(colors.backgroundTertiary)
        )
        IconButton(
            onClick = onPlusClicked,
            modifier = Modifier
                .size(36.dp),
            enabled = count < 99
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.ic_add),
                tint = if (count < 99) colors.foregroundPrimary else colors.foregroundSecondary,
                contentDescription = stringResource(id = R.string.common_general_email),
            )
        }
    }
}

@Composable
@Preview(showBackground = true)
fun PreviewLightCounter() {
    ThemedCounter(false)
}

@Composable
@Preview(showBackground = true)
fun PreviewDarkCounter() {
    ThemedCounter(true)
}

@Composable
private fun ThemedCounter(
    isDarkTheme: Boolean
) {
    ChefBookTheme(darkTheme = isDarkTheme) {
        Surface(
            color = ChefBookTheme.colors.backgroundPrimary
        ) {
            Counter(
                count = 1,
                onMinusClicked = { /*TODO*/ },
                onPlusClicked = { /*TODO*/ }
            )
        }
    }
}