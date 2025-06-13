package io.chefbook.ui.design.components.textfields.internal

import androidx.compose.foundation.interaction.Interaction
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.takeOrElse
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation

const val SingleLine = 1

/**
 * Analogue of standard TextField with more freedom. Instead of original it has modifiable paddings
 * and doesn't apply background, minimum size and indicator line modifiers. You can add them by pass
 * suitable modifier by default. Also singleLine argument reduced to maxLines.
 *
 * @param value the input text to be shown in the text field
 * @param onValueChange the callback that is triggered when the input service updates the text. An
 * updated text comes as a parameter of the callback
 * @param modifier a [Modifier] for this text field
 * @param enabled controls the enabled state of the [BasicTextField]. When `false`, the text field will
 * be neither editable nor focusable, the input of the text field will not be selectable,
 * visually text field will appear in the disabled UI state
 * @param readOnly controls the editable state of the [BasicTextField]. When `true`, the text
 * field can not be modified, however, a user can focus it and copy text from it. Read-only text
 * fields are usually used to display pre-filled forms that user can not edit
 * @param textStyle the style to be applied to the input text. The default [textStyle] uses the
 * [LocalTextStyle] defined by the theme
 * @param label the optional label to be displayed inside the text field container.
 * @param placeholder the optional placeholder to be displayed when the text field is in focus and
 * the input text is empty.
 * @param lead the optional leading icon to be displayed at the beginning of the text field
 * container
 * @param trail the optional trailing icon to be displayed at the end of the text field
 * container
 * @param prefix the optional prefix to be displayed before the input text in the text field
 * @param suffix the optional suffix to be displayed after the input text in the text field
 * @param supportingText the optional supporting text to be displayed below the text field
 * @param isError indicates if the text field's current value is in error. If set to true, the
 * label, bottom indicator and trailing icon by default will be displayed in error color
 * @param visualTransformation transforms the visual representation of the input [value]
 * For example, you can use
 * [PasswordVisualTransformation][androidx.compose.ui.text.input.PasswordVisualTransformation] to
 * create a password text field. By default no visual transformation is applied
 * @param keyboardOptions software keyboard options that contains configuration such as
 * [KeyboardType] and [ImeAction].
 * @param keyboardActions when the input service emits an IME action, the corresponding callback
 * is called. Note that this IME action may be different from what you specified in
 * [KeyboardOptions.imeAction].
 * @param maxLines the maximum height in terms of maximum number of visible lines. Should be
 * equal or greater than 1.
 * @param minLines the minimum height in terms of minimum number of visible lines. It is required
 *   that 1 <= [minLines] <= [maxLines]. This parameter is ignored when [maxLines] is 1.
 * @param interactionSource the [MutableInteractionSource] representing the stream of
 * [Interaction]s for this TextField. You can create and pass in your own remembered
 * [MutableInteractionSource] if you want to observe [Interaction]s and customize the
 * appearance / behavior of this TextField in different [Interaction]s.
 * @param colors [TextFieldColors] that will be used to resolve color of the text, content
 * (including label, placeholder, leading and trailing icons, indicator line) and background for
 * this text field in different states. See [TextFieldDefaults.colors]
 * @param contentPadding [PaddingValues] that will be applied to actual text area
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun TextField(
  value: String,
  onValueChange: (String) -> Unit,
  modifier: Modifier = Modifier,
  enabled: Boolean = true,
  readOnly: Boolean = false,
  textStyle: TextStyle = LocalTextStyle.current,
  label: @Composable (() -> Unit)? = null,
  placeholder: @Composable (() -> Unit)? = null,
  lead: @Composable (() -> Unit)? = null,
  trail: @Composable (() -> Unit)? = null,
  prefix: @Composable (() -> Unit)? = null,
  suffix: @Composable (() -> Unit)? = null,
  supportingText: @Composable (() -> Unit)? = null,
  isError: Boolean = false,
  visualTransformation: VisualTransformation = VisualTransformation.None,
  keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
  keyboardActions: KeyboardActions = KeyboardActions(),
  maxLines: Int = Int.MAX_VALUE,
  minLines: Int = SingleLine,
  interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
  shape: Shape = TextFieldDefaults.shape,
  colors: TextFieldColors = TextFieldDefaults.colors(),
  contentPadding: PaddingValues =
    if (label == null) {
      TextFieldDefaults.contentPaddingWithoutLabel()
    } else {
      TextFieldDefaults.contentPaddingWithLabel()
    },
  decorationBoxStyle: DecorationBoxStyle = DecorationBoxStyle.Default,
) {
  val textColor = textStyle.color.takeOrElse {
    val isFocused = interactionSource.collectIsFocusedAsState().value
    when {
      !enabled -> colors.disabledTextColor
      isError -> colors.errorTextColor
      isFocused -> colors.focusedTextColor
      else -> colors.unfocusedTextColor
    }
  }
  val mergedTextStyle = textStyle.merge(TextStyle(color = textColor))

  BasicTextField(
    value = value,
    onValueChange = onValueChange,
    modifier = modifier,
    enabled = enabled,
    readOnly = readOnly,
    textStyle = mergedTextStyle,
    cursorBrush = SolidColor(
      when {
        isError -> colors.errorCursorColor
        else -> colors.cursorColor
      }
    ),
    visualTransformation = visualTransformation,
    keyboardOptions = keyboardOptions,
    keyboardActions = keyboardActions,
    interactionSource = interactionSource,
    singleLine = maxLines == 1,
    maxLines = maxLines,
    minLines = minLines,
    decorationBox = @Composable { innerTextField ->
      when (decorationBoxStyle) {
        DecorationBoxStyle.Default -> TextFieldDefaults.DecorationBox(
          value = value,
          visualTransformation = visualTransformation,
          innerTextField = innerTextField,
          placeholder = placeholder,
          label = label,
          leadingIcon = lead,
          trailingIcon = trail,
          prefix = prefix,
          suffix = suffix,
          supportingText = supportingText,
          shape = shape,
          singleLine = maxLines == SingleLine,
          enabled = enabled,
          isError = isError,
          interactionSource = interactionSource,
          colors = colors,
          contentPadding = contentPadding,
        )
        DecorationBoxStyle.Outlined -> OutlinedTextFieldDefaults.DecorationBox(
          value = value,
          visualTransformation = visualTransformation,
          innerTextField = innerTextField,
          placeholder = placeholder,
          label = label,
          leadingIcon = lead,
          trailingIcon = trail,
          prefix = prefix,
          suffix = suffix,
          supportingText = supportingText,
          singleLine = maxLines == SingleLine,
          enabled = enabled,
          isError = isError,
          interactionSource = interactionSource,
          colors = colors,
          contentPadding = contentPadding,
          container = {
            OutlinedTextFieldDefaults.Container(
              enabled = enabled,
              isError = isError,
              interactionSource = interactionSource,
              colors = colors,
              shape = shape,
            )
          }
        )
      }
    }
  )
}
