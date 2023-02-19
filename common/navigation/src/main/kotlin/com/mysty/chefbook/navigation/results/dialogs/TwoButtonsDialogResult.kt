package com.mysty.chefbook.navigation.results.dialogs

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
sealed class TwoButtonsDialogResult(
    open val request: String? = null,
) : Parcelable {

    data class LeftButtonClicked(
        override val request: String? = null,
    ) : TwoButtonsDialogResult(request)

    data class RightButtonClicked(
        override val request: String? = null,
    ) : TwoButtonsDialogResult(request)
}