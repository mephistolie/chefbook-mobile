package io.chefbook.features.profile.editing.ui.mvi

import io.chefbook.libs.mvi.MviState

internal data class ProfileEditingScreenState(
  val avatar: String? = null,
  val nickname: String = "",
  val nicknameValid: Boolean = true,
  val nicknameHint: String? = null,
  val firstName: String = "",
  val lastName: String = "",
  val description: String = "",
  val isOnline: Boolean = false,
  val isConfirmButtonAvailable: Boolean = false,
  val isLoading: Boolean = false,
) : MviState
