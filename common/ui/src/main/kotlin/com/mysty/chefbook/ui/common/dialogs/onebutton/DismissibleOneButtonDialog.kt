package com.mysty.chefbook.ui.common.dialogs.onebutton

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import com.mysty.chefbook.core.constants.Strings
import com.mysty.chefbook.navigation.params.dialogs.OneButtonDialogParams
import com.mysty.chefbook.navigation.results.dialogs.OneButtonDialogResult
import com.mysty.chefbook.navigation.styles.DismissibleDialog
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.result.ResultBackNavigator

@Destination("one_button_dialog_dismissible", style = DismissibleDialog::class)
@Composable
fun DismissibleOneButtonDialog(
    params: OneButtonDialogParams,
    request: String = Strings.EMPTY,
    resultBackNavigator: ResultBackNavigator<OneButtonDialogResult>,
) {
    OneButtonDialogContent(
        title = stringResource(params.titleId),
        description = params.descriptionId?.let { stringResource(id = it) },
        buttonIcon = ImageVector.vectorResource(params.buttonIconId),
        buttonText = stringResource(id = params.buttonTextId),

        onClick = { resultBackNavigator.navigateBack(OneButtonDialogResult(request)) },
    )
}
