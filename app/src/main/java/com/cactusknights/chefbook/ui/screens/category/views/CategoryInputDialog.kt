package com.mysty.chefbook.design.components.dialogs

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.cactusknights.chefbook.R
import com.mysty.chefbook.api.category.domain.entities.CategoryInput
import com.mysty.chefbook.core.constants.Strings
import com.mysty.chefbook.core.ui.compose.providers.theme.LocalTheme
import com.mysty.chefbook.design.components.buttons.DynamicButton
import com.mysty.chefbook.design.components.textfields.FilledTextField
import com.mysty.chefbook.design.theme.ChefBookTheme
import com.mysty.chefbook.design.theme.shapes.DialogShape

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun CategoryInputDialog(
    onCancel: () -> Unit,
    onConfirm: (CategoryInput) -> Unit,
    modifier: Modifier = Modifier,
    initName: String? = null,
    initCover: String? = null,
    isEditing: Boolean = false,
    onDelete: (CategoryInput) -> Unit = {},
) {
    val context = LocalContext.current
    val resources = context.resources

    val colors = LocalTheme.colors
    val typography = LocalTheme.typography

    val coverRegex = Regex("^.*[а-яА-ЯёЁa-zA-Z0-9!@#\$%ˆ&*()_+-=\"№;%:?*]+?")

    var name by remember { mutableStateOf(if (isEditing) initName.orEmpty() else Strings.EMPTY) }
    var cover by remember { mutableStateOf(if (isEditing) initCover.orEmpty() else Strings.EMPTY) }

    Dialog(
        onDismissRequest = onCancel,
        properties = DialogProperties(
            usePlatformDefaultWidth = false
        )
    ) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(12.dp)
                .background(
                    color = colors.backgroundPrimary,
                    shape = DialogShape,
                )
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = stringResource(
                    if (isEditing) {
                        R.string.common_category_input_dialog_edit_category
                    } else {
                        R.string.common_category_input_dialog_new_category
                    }
                ),
                style = typography.h3,
                color = colors.foregroundPrimary
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(top = 18.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                FilledTextField(
                    value = cover,
                    onValueChange = { text ->
                        if (
                            (cover.length > text.length || cover.isEmpty()) &&
                            (text.length <= 10 && !coverRegex.matches(text) || text.length <= 1)
                        ) {
                            cover = text
                        }
                    },
                    modifier = modifier.width(56.dp),
                    textOpacity = if (cover.isNotEmpty()) 1F else 0.5F,
                    hint = "\uD83E\uDD66",
                )
                FilledTextField(
                    value = name,
                    onValueChange = { text -> name = text },
                    modifier = modifier.fillMaxWidth(),
                    hint = stringResource(R.string.common_general_name),
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(top = 24.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                if (isEditing) {
                    DynamicButton(
                        leftIcon = ImageVector.vectorResource(R.drawable.ic_trash),
                        unselectedForeground = colors.foregroundPrimary,
                        modifier = Modifier
                            .padding(end = 8.dp)
                            .width(56.dp)
                            .height(44.dp),
                        onClick = {
                            onDelete(
                                CategoryInput(
                                    name = name,
                                    cover = cover.ifEmpty { null },
                                )
                            )
                        },
                    )
                }
                DynamicButton(
                    leftIcon = ImageVector.vectorResource(R.drawable.ic_cross),
                    unselectedForeground = colors.foregroundPrimary,
                    modifier = Modifier
                        .weight(1F)
                        .fillMaxWidth()
                        .height(44.dp),
                    onClick = onCancel,
                )
                DynamicButton(
                    leftIcon = ImageVector.vectorResource(R.drawable.ic_check),
                    isSelected = name.isNotEmpty(),
                    modifier = Modifier
                        .weight(1F)
                        .fillMaxWidth()
                        .height(44.dp),
                    onClick = {
                        if (name.isNotEmpty()) {
                            onConfirm(
                                CategoryInput(
                                    name = name,
                                    cover = cover.ifEmpty { null },
                                )
                            )
                        } else {
                            Toast.makeText(
                                context,
                                resources.getString(R.string.common_category_input_dialog_enter_name),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                )
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
private fun PreviewLightCategoryInputDialog() {
    ThemedCategoryInputDialog(false, isDarkTheme = false)
}

@Composable
@Preview(showBackground = true)
private fun PreviewDarkCategoryInputDialog() {
    ThemedCategoryInputDialog(true, isDarkTheme = true)
}

@Composable
private fun ThemedCategoryInputDialog(
    isEditing: Boolean,
    isDarkTheme: Boolean
) {
    ChefBookTheme(darkTheme = isDarkTheme) {
        Surface(
            color = LocalTheme.colors.backgroundPrimary,
            modifier = Modifier.fillMaxWidth()
        ) {
            CategoryInputDialog(
                initName = "Meat",
                initCover = "\uD83E\uDD69",
                onCancel = {},
                onConfirm = {},
                isEditing = isEditing,
                onDelete = {},
            )
        }
    }
}
