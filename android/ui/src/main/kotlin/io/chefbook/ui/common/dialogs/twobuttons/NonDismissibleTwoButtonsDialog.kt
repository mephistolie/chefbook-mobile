package io.chefbook.ui.common.dialogs.twobuttons

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.result.ResultBackNavigator
import io.chefbook.navigation.params.dialogs.TwoButtonsDialogParams
import io.chefbook.navigation.results.dialogs.TwoButtonsDialogResult
import io.chefbook.navigation.styles.NonDismissibleDialog

@Destination("two_buttons_dialog_non_dismissible", style = NonDismissibleDialog::class)
@Composable
fun NonDismissibleTwoButtonsDialog(
    params: TwoButtonsDialogParams,
    request: String = "",
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
