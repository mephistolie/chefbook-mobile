package io.chefbook.navigation.params.dialogs

import android.os.Parcelable
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import io.chefbook.core.commonGeneralAreYouSure
import io.chefbook.design.R
import kotlinx.parcelize.Parcelize
import io.chefbook.core.Res as CoreR

@Parcelize
data class TwoButtonsDialogParams(
//  val nonDismissible: Boolean = false,
//  @StringRes
//  val titleId: Int = CoreR.string.commonGeneralAreYouSure,
  @StringRes
  val descriptionId: Int? = null,
//  @DrawableRes
//  val leftButtonIconId: Int? = R.drawable.ic_cross,
  @StringRes
  val leftButtonTextId: Int? = null,
  val isLeftButtonPrimary: Boolean = false,
//  @DrawableRes
//  val rightButtonIconId: Int? = R.drawable.ic_check,
  @StringRes
  val rightButtonTextId: Int? = null,
  val isRightButtonPrimary: Boolean = true,
) : Parcelable
