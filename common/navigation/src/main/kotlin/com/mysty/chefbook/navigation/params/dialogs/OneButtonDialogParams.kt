package com.mysty.chefbook.navigation.params.dialogs

import android.os.Parcelable
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.mysty.chefbook.design.R
import kotlinx.parcelize.Parcelize

@Parcelize
data class OneButtonDialogParams(
    val nonDismissible: Boolean = false,
    @StringRes
    val titleId: Int = R.string.common_general_are_you_sure,
    @StringRes
    val descriptionId: Int? = null,
    @DrawableRes
    val buttonIconId: Int = R.drawable.ic_check,
    @StringRes
    val buttonTextId: Int = R.string.common_general_ok,
    val isButtonSelected: Boolean = true,
    val fullWidthButton: Boolean = false,
) : Parcelable
