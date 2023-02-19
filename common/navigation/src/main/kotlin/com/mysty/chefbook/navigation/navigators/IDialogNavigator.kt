package com.mysty.chefbook.navigation.navigators

import com.mysty.chefbook.core.constants.Strings
import com.mysty.chefbook.navigation.params.dialogs.OneButtonDialogParams
import com.mysty.chefbook.navigation.params.dialogs.TwoButtonsDialogParams

interface IDialogNavigator: IBaseNavigator {
    fun openOneButtonDialog(
        params: OneButtonDialogParams = OneButtonDialogParams(),
        request: String = Strings.EMPTY,
    )
    fun openTwoButtonsDialog(
        params: TwoButtonsDialogParams = TwoButtonsDialogParams(),
        request: String = Strings.EMPTY,
    )
    fun openErrorInfoDialog(
        error: Throwable? = null,
        dialogParams: OneButtonDialogParams = OneButtonDialogParams(),
        request: String = Strings.EMPTY,
    )
    fun openErrorDialog(
        error: Throwable? = null,
        dialogParams: TwoButtonsDialogParams = TwoButtonsDialogParams(),
        request: String = Strings.EMPTY,
    )
}
