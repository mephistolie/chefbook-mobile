package com.cactusknights.chefbook.ui.views.textfields

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Surface
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cactusknights.chefbook.common.Strings
import com.cactusknights.chefbook.ui.themes.ChefBookTheme
import com.mephistolie.compost.ui.textfields.TextField

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun PinCodeField(
    value: Char?,
    onPinCodeNumAdd: (Int) -> Unit,
    onPinCodeNumRemove: () -> Unit,
    modifier: Modifier = Modifier,
    isFocused: Boolean = false,
) {
    val colors = ChefBookTheme.colors
    val typography = ChefBookTheme.typography

    val shape = RoundedCornerShape(8.dp)

    val focusedTransition = updateTransition(isFocused, label = "isFocused")
    val borderColor by focusedTransition.animateColor(label = "borderColor") { selected ->
        if (selected) colors.tintPrimary else colors.backgroundSecondary
    }

    val valueTransition = updateTransition(value, label = "hasValue")
    val textOffset by valueTransition.animateDp(label = "textOffset") { input ->
        if (input != null) (-2).dp else 48.dp
    }


    Box(
        modifier = modifier
            .width(32.dp)
            .height(48.dp)
            .clip(shape)
            .border(
                width = 2.dp,
                brush = SolidColor(borderColor),
                shape = shape,
            ),
        contentAlignment = Alignment.Center,
    ) {
        TextField(
            value = value?.toString() ?: Strings.EMPTY,
            onValueChange = { it.toIntOrNull()?.let(onPinCodeNumAdd) },
            cursorBrush = SolidColor(Color.Transparent),
            contentPadding = PaddingValues(0.dp),
            visualTransformation = PasswordVisualTransformation(),
            colors = TextFieldDefaults.textFieldColors(
                textColor = colors.foregroundPrimary,
                disabledTextColor = colors.foregroundPrimary,
            ),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.None
            ),
            textStyle = typography.h1.copy(
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Medium,
            ),
            modifier = Modifier
                .onKeyEvent { event ->
                    if (event.type == KeyEventType.KeyUp && event.key == Key.Backspace) {
                        onPinCodeNumRemove()
                        return@onKeyEvent true
                    }
                    false
                }
                .offset(y = textOffset)
        )
    }
}

@Composable
@Preview(showBackground = true)
fun PreviewLightPinCodeField() {
    ThemedPinCodeField(
        isDarkTheme = false,
        isFocused = true,
    )
}

@Composable
@Preview(showBackground = true)
fun PreviewDarkPinCodeField() {
    ThemedPinCodeField(
        isDarkTheme = true,
        isFocused = false,
    )
}

@Composable
private fun ThemedPinCodeField(
    isDarkTheme: Boolean,
    isFocused: Boolean,
) {
    ChefBookTheme(darkTheme = isDarkTheme) {
        Surface(
            color = ChefBookTheme.colors.backgroundPrimary
        ) {
            PinCodeField(
                value = '1',
                onPinCodeNumAdd = {},
                onPinCodeNumRemove = {},
                isFocused = isFocused,
            )
        }
    }
}
