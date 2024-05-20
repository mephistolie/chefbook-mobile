package io.chefbook.features.profile.deletion.ui.mvi

import io.chefbook.libs.mvi.MviIntent

internal sealed interface ProfileDeletionScreenIntent : MviIntent {

  data class SetPassword(val password: String) : ProfileDeletionScreenIntent
  data object SwitchSharedDataDeletionStatus :
    ProfileDeletionScreenIntent

  data object RequestProfileDeletion : ProfileDeletionScreenIntent
  data object Close : ProfileDeletionScreenIntent
}
