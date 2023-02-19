package com.mysty.chefbook.ui.common.dialogs.twobuttons

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import com.mysty.chefbook.core.constants.Strings
import com.mysty.chefbook.navigation.params.dialogs.TwoButtonsDialogParams
import com.mysty.chefbook.navigation.results.dialogs.TwoButtonsDialogResult
import com.mysty.chefbook.navigation.styles.DismissibleDialog
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.result.ResultBackNavigator

@Destination("two_buttons_dialog_dismissible", style = DismissibleDialog::class)
@Composable
fun DismissibleTwoButtonsDialog(
    params: TwoButtonsDialogParams,
    request: String = Strings.EMPTY,
    resultBackNavigator: ResultBackNavigator<TwoButtonsDialogResult>,
) {
    TwoButtonsDialogContent(
        title = stringResource(params.titleId),
        description = params.descriptionId?.let { stringResource(it) },
        leftButtonIcon = params.leftButtonIconId?.let { ImageVector.vectorResource(it) },
        leftButtonText = params.leftButtonTextId?.let { stringResource(it) },
        isLeftButtonPrimary = params.isLeftButtonPrimary,
        rightButtonIcon =  params.rightButtonIconId?.let { ImageVector.vectorResource(it) },
        rightButtonText =  params.rightButtonTextId?.let { stringResource(it) },
        isRightButtonPrimary = params.isRightButtonPrimary,

        onLeftClick = { resultBackNavigator.navigateBack(TwoButtonsDialogResult.LeftButtonClicked(request = request)) },
        onRightClick = { resultBackNavigator.navigateBack(TwoButtonsDialogResult.RightButtonClicked(request = request)) },
    )
}
