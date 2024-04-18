package io.chefbook.features.profile.deletion.ui.mvi

import io.chefbook.libs.mvi.MviState

internal data class ProfileDeletionScreenState(
  val password: String = "",
  val deleteSharedData: Boolean = false,
) : MviState
