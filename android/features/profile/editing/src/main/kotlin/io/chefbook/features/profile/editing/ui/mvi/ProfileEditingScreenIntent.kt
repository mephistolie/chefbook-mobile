package io.chefbook.features.profile.editing.ui.mvi

import io.chefbook.libs.mvi.MviIntent

internal sealed interface ProfileEditingScreenIntent : MviIntent {

  data class SetAvatar(val avatar: String) : ProfileEditingScreenIntent
  data object DeleteAvatar : ProfileEditingScreenIntent

  data class SetNickname(val nickname: String) : ProfileEditingScreenIntent
  data class SetFirstName(val firstName: String) : ProfileEditingScreenIntent
  data class SetLastName(val lastName: String) : ProfileEditingScreenIntent
  data class SetDescription(val description: String) : ProfileEditingScreenIntent

  data object Confirm : ProfileEditingScreenIntent
  data object Close : ProfileEditingScreenIntent
}
