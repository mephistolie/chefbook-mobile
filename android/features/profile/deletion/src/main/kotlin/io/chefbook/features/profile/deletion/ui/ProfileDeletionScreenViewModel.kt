package io.chefbook.features.profile.deletion.ui

import android.content.Context
import io.chefbook.features.profile.deletion.R
import io.chefbook.features.profile.deletion.ui.mvi.ProfileDeletionScreenEffect
import io.chefbook.features.profile.deletion.ui.mvi.ProfileDeletionScreenIntent
import io.chefbook.features.profile.deletion.ui.mvi.ProfileDeletionScreenState
import io.chefbook.libs.mvi.BaseMviViewModel
import io.chefbook.sdk.profile.api.external.domain.usecases.RequestProfileDeletionUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

internal class ProfileDeletionScreenViewModel(
  private val requestProfileDeletionUseCase: RequestProfileDeletionUseCase,
  context: Context,
) : BaseMviViewModel<ProfileDeletionScreenState, ProfileDeletionScreenIntent, ProfileDeletionScreenEffect>() {

  private val invalidPasswordMessage = context.getString(R.string.common_profile_deletion_screen_invalid_password)

  override val _state: MutableStateFlow<ProfileDeletionScreenState> =
    MutableStateFlow(ProfileDeletionScreenState())

  override suspend fun reduceIntent(intent: ProfileDeletionScreenIntent) {
    when (intent) {
      is ProfileDeletionScreenIntent.SetPassword ->
        _state.update { it.copy(password = intent.password) }

      is ProfileDeletionScreenIntent.SwitchSharedDataDeletionStatus ->
        _state.update { it.copy(deleteSharedData = !it.deleteSharedData) }

      is ProfileDeletionScreenIntent.RequestProfileDeletion -> requestProfileDeletion()
      is ProfileDeletionScreenIntent.Close -> _effect.emit(ProfileDeletionScreenEffect.Closed)
    }
  }

  private suspend fun requestProfileDeletion() {
    val state = state.value
    requestProfileDeletionUseCase(state.password, deleteSharedData = state.deleteSharedData)
      .onFailure {
        _state.update { it.copy(password = "") }
        _effect.emit(ProfileDeletionScreenEffect.ToastShown(invalidPasswordMessage))
      }
  }
}
