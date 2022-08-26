package com.cactusknights.chefbook.ui.views.textfields

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.TextFieldDefaults.indicatorLine
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

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun IndicatorTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    textStyle: TextStyle = ChefBookTheme.typography.body1,
    placeholder: @Composable (() -> Unit)? = null,
    label: @Composable (() -> Unit)? = null,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions(),
    maxLines: Int = Int.MAX_VALUE,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    focusedIndicatorColor: Color = ChefBookTheme.colors.tintPrimary,
) {
    val colors = ChefBookTheme.colors
    val typography = ChefBookTheme.typography

    val fieldColors = TextFieldDefaults.textFieldColors(
        textColor = colors.foregroundPrimary,
        cursorColor = colors.tintPrimary,
        focusedLabelColor = focusedIndicatorColor,
        focusedIndicatorColor = colors.tintPrimary,
        disabledIndicatorColor = colors.backgroundSecondary,
        unfocusedIndicatorColor = colors.backgroundSecondary,
        backgroundColor = Color.Transparent
    )

    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier
            .indicatorLine(
                enabled = true,
                isError = false,
                interactionSource = interactionSource,
                colors = fieldColors,
                focusedIndicatorLineThickness = 1.dp,
                unfocusedIndicatorLineThickness = 1.dp,
            ),
        enabled = enabled,
        readOnly = readOnly,
        textStyle = textStyle,
        cursorBrush = SolidColor(colors.tintPrimary),
        visualTransformation = visualTransformation,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        interactionSource = interactionSource,
        singleLine = maxLines == 1,
        maxLines = maxLines,
        decorationBox = @Composable { innerTextField ->
            TextFieldDefaults.TextFieldDecorationBox(
                value = value,
                visualTransformation = visualTransformation,
                innerTextField = innerTextField,
                placeholder = placeholder,
                label = label,
                singleLine = maxLines == 1,
                enabled = enabled,
                interactionSource = interactionSource,
                colors = fieldColors,
                contentPadding = if (label == null) {
                    PaddingValues(vertical = 16.dp)
                } else {
                    PaddingValues(
                        start = 0.dp,
                        top = 20.dp,
                        end = 0.dp,
                        bottom = 10.dp,
                    )
                }
            )
        }
    )
}

@Composable
@Preview(showBackground = true)
fun PreviewLightIndicatorFields() {
    ThemedIndicatorFields(false)
}

@Composable
@Preview(showBackground = true)
fun PreviewDarkIndicatorFields() {
    ThemedIndicatorFields(true)
}

@Composable
private fun ThemedIndicatorFields(
    isDarkTheme: Boolean
) {
    ChefBookTheme(darkTheme = isDarkTheme) {
        Surface(
            color = ChefBookTheme.colors.backgroundPrimary
        ) {
            Column {
                IndicatorTextField(
                    value = "",
                    onValueChange = {},
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text(stringResource(R.string.common_general_email)) },
                )
                Spacer(Modifier.height(8.dp))
                IndicatorTextField(
                    value = "",
                    onValueChange = {},
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text(stringResource(R.string.common_general_password)) },
                )
            }
        }
    }
}