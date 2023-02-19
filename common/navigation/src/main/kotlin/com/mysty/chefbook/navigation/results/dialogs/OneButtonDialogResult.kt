package com.mysty.chefbook.navigation.results.dialogs

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class OneButtonDialogResult(
  val request: String? = null
) : Parcelable
