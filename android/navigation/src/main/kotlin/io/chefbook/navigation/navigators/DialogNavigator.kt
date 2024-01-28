package io.chefbook.navigation.navigators

import io.chefbook.navigation.params.dialogs.OneButtonDialogParams
import io.chefbook.navigation.params.dialogs.TwoButtonsDialogParams

interface DialogNavigator: BaseNavigator {
    fun openOneButtonDialog(
        params: OneButtonDialogParams = OneButtonDialogParams(),
        request: String = "",
    )
    fun openTwoButtonsDialog(
        params: TwoButtonsDialogParams = TwoButtonsDialogParams(),
        request: String = "",
    )
    fun openErrorInfoDialog(
        error: Throwable? = null,
        dialogParams: OneButtonDialogParams = OneButtonDialogParams(),
        request: String = "",
    )
    fun openErrorDialog(
        error: Throwable? = null,
        dialogParams: TwoButtonsDialogParams = TwoButtonsDialogParams(),
        request: String = "",
    )
}
