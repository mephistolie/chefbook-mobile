package com.cactusknights.chefbook.ui.views.textfields

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cactusknights.chefbook.R
import com.cactusknights.chefbook.ui.themes.ChefBookTheme
import com.mephistolie.compost.ui.textfields.IndicatorLineTextField

@Composable
fun ChefBookIndicatorTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    textStyle: TextStyle = ChefBookTheme.typography.body1,
    label: @Composable (() -> Unit)? = null,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions(),
    maxLines: Int = Int.MAX_VALUE,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    focusedIndicatorColor: Color = ChefBookTheme.colors.tintPrimary,
) {
    val colors = ChefBookTheme.colors

    val fieldColors = TextFieldDefaults.textFieldColors(
        textColor = colors.foregroundPrimary,
        cursorColor = colors.tintPrimary,
        focusedLabelColor = focusedIndicatorColor,
        focusedIndicatorColor = colors.tintPrimary,
        disabledIndicatorColor = colors.backgroundSecondary,
        unfocusedIndicatorColor = colors.backgroundSecondary,
        backgroundColor = Color.Transparent
    )

    IndicatorLineTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        enabled = enabled,
        readOnly = readOnly,
        textStyle = textStyle,
        cursorBrush = SolidColor(colors.tintPrimary),
        label = label,
        visualTransformation = visualTransformation,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        interactionSource = interactionSource,
        maxLines = maxLines,
        indicatorLineEnabled = true,
        colors = fieldColors,
        focusedIndicatorLineThickness = 1.dp,
        unfocusedIndicatorLineThickness = 1.dp,
    )
}

@Composable
@Preview(showBackground = true)
fun PreviewLightIndicatorFields() {
    ThemedChefBookIndicatorFields(false)
}

@Composable
@Preview(showBackground = true)
fun PreviewDarkIndicatorFields() {
    ThemedChefBookIndicatorFields(true)
}

@Composable
private fun ThemedChefBookIndicatorFields(
    isDarkTheme: Boolean
) {
    ChefBookTheme(darkTheme = isDarkTheme) {
        Surface(
            color = ChefBookTheme.colors.backgroundPrimary
        ) {
            Column {
                ChefBookIndicatorTextField(
                    value = "",
                    onValueChange = {},
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text(stringResource(R.string.common_general_email)) },
                )
                Spacer(Modifier.height(8.dp))
                ChefBookIndicatorTextField(
                    value = "",
                    onValueChange = {},
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text(stringResource(R.string.common_general_password)) },
                )
            }
        }
    }
}