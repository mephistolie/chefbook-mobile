package io.chefbook.features.profile.editing.ui

import android.content.Context
import android.net.Uri
import androidx.lifecycle.viewModelScope
import io.chefbook.features.profile.editing.R
import io.chefbook.features.profile.editing.ui.mvi.ProfileEditingScreenEffect
import io.chefbook.features.profile.editing.ui.mvi.ProfileEditingScreenIntent
import io.chefbook.features.profile.editing.ui.mvi.ProfileEditingScreenState
import io.chefbook.libs.coroutines.AppDispatchers
import io.chefbook.libs.mvi.BaseMviViewModel
import io.chefbook.libs.mvi.MviViewModel
import io.chefbook.libs.utils.auth.isFirstNicknameSymbol
import io.chefbook.libs.utils.auth.isLastNicknameSymbol
import io.chefbook.libs.utils.auth.isNickname
import io.chefbook.libs.utils.auth.isNicknameSymbols
import io.chefbook.sdk.profile.api.external.domain.entities.Profile
import io.chefbook.sdk.profile.api.external.domain.usecases.CheckNicknameAvailabilityUseCase
import io.chefbook.sdk.profile.api.external.domain.usecases.DeleteAvatarUseCase
import io.chefbook.sdk.profile.api.external.domain.usecases.ObserveProfileUseCase
import io.chefbook.sdk.profile.api.external.domain.usecases.SetAvatarUseCase
import io.chefbook.sdk.profile.api.external.domain.usecases.SetDescriptionUseCase
import io.chefbook.sdk.profile.api.external.domain.usecases.SetNameUseCase
import io.chefbook.sdk.profile.api.external.domain.usecases.SetNicknameUseCase
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.File

internal class ProfileEditingScreenViewModel(
  context: Context,
  observeProfileUseCase: ObserveProfileUseCase,
  private val setAvatarUseCase: SetAvatarUseCase,
  private val deleteAvatarUseCase: DeleteAvatarUseCase,
  private val checkNicknameAvailabilityUseCase: CheckNicknameAvailabilityUseCase,
  private val setNicknameUseCase: SetNicknameUseCase,
  private val setNameUseCase: SetNameUseCase,
  private val setDescriptionUseCase: SetDescriptionUseCase,
  dispatchers: AppDispatchers,
) :
  BaseMviViewModel<ProfileEditingScreenState, ProfileEditingScreenIntent, ProfileEditingScreenEffect>() {

  private val resources = context.resources

  private var checkNicknameJob: Job? = null

  private var profile: Profile? = null
    set(value) {
      if (field == null && value != null) {
        _state.tryEmit(
          ProfileEditingScreenState(
            avatar = value.avatar,
            nickname = value.nickname.orEmpty(),
            nicknameHint = getNicknameHint(value.nickname.orEmpty()),
            firstName = value.firstName.orEmpty(),
            lastName = value.lastName.orEmpty(),
            description = value.description.orEmpty(),
            isOnline = value.isOnline,
          )
        )
      }
      field = value
    }

  override val _state: MutableStateFlow<ProfileEditingScreenState> = MutableStateFlow(
    ProfileEditingScreenState()
  )

  init {
    viewModelScope.launch {
      launch(dispatchers.io) { prepareCacheDir(context) }
      observeProfileUseCase()
        .filterNotNull()
        .collect { profile -> this@ProfileEditingScreenViewModel.profile = profile }
    }
  }

  override suspend fun reduceIntent(intent: ProfileEditingScreenIntent) {
    when (intent) {
      is ProfileEditingScreenIntent.SetAvatar -> _state.update {
        it.copy(avatar = intent.avatar, isConfirmButtonAvailable = isInputValid(it.nickname))
      }

      is ProfileEditingScreenIntent.DeleteAvatar -> _state.update {
        it.copy(avatar = null, isConfirmButtonAvailable = isInputValid(it.nickname))
      }

      is ProfileEditingScreenIntent.SetNickname -> setNickname(intent.nickname.trim())
      is ProfileEditingScreenIntent.SetFirstName -> {
        if (intent.firstName.length <= MAX_NAME_LENGTH) _state.update {
          it.copy(firstName = intent.firstName.trim(), isConfirmButtonAvailable = isInputValid(it.nickname))
        }
      }

      is ProfileEditingScreenIntent.SetLastName -> {
        if (intent.lastName.length <= MAX_NAME_LENGTH) _state.update {
          it.copy(lastName = intent.lastName.trim(), isConfirmButtonAvailable = isInputValid(it.nickname))
        }
      }

      is ProfileEditingScreenIntent.SetDescription -> {
        if (intent.description.length <= MAX_DESCRIPTION_LENGTH) _state.update {
          it.copy(description = intent.description, isConfirmButtonAvailable = isInputValid(it.nickname))
        }
      }

      is ProfileEditingScreenIntent.DeleteProfile -> _effect.emit(ProfileEditingScreenEffect.ProfileDeletionScreenOpened)

      is ProfileEditingScreenIntent.Confirm -> confirmInput()
      is ProfileEditingScreenIntent.Close -> _effect.emit(ProfileEditingScreenEffect.Closed)
    }
  }

  private suspend fun setNickname(nickname: String) {
    if (!isFirstNicknameSymbol(nickname.firstOrNull())) return
    if (!isNicknameSymbols(nickname)) return
    if (nickname.length > MAX_NICKNAME_LENGTH) return

    _state.update {
      it.copy(
        nickname = nickname,
        nicknameValid = isInputValid(nickname),
        nicknameHint = getNicknameHint(nickname),
        isConfirmButtonAvailable = isInputValid(nickname)
      )
    }
    checkNicknameAvailability()
  }

  private fun getNicknameHint(nickname: String) =
    when {
      nickname.length in 1..4 -> resources.getString(R.string.common_profile_editing_screen_nickname_short)
      !isLastNicknameSymbol(nickname.lastOrNull()) -> resources.getString(R.string.common_profile_editing_screen_nickname_last_symbol)
      isNickname(nickname) -> resources.getString(R.string.common_profile_editing_screen_nickname_link)
      else -> null
    }

  private suspend fun checkNicknameAvailability() {
    checkNicknameJob?.cancel()
    checkNicknameJob = viewModelScope.launch {
      delay(500)
      val nickname = state.value.nickname
      if (isNickname(nickname)) checkNicknameAvailabilityUseCase(state.value.nickname)
        .onFailure { e ->
          if (e is CancellationException) return@onFailure
          _state.update { state ->
            state.copy(
              nicknameValid = false,
              nicknameHint = resources.getString(R.string.common_profile_editing_screen_nickname_not_available),
              isConfirmButtonAvailable = false,
            )
          }
        }
    }
  }

  private suspend fun confirmInput() {
    _state.update { it.copy(isLoading = true) }
    val state = state.value
    coroutineScope {
      launch {
        if (state.avatar != profile?.avatar) {
          when {
            state.avatar != null -> setAvatarUseCase(state.avatar)
            else -> deleteAvatarUseCase()
          }
        }
      }
      launch {
        val nickname = state.nickname
        if (nickname != profile?.nickname && isNickname(nickname)) setNicknameUseCase(nickname.trim())
      }
      launch {
        val firstName = state.firstName.ifBlank { null }
        val lastName = state.lastName.ifBlank { null }
        if (firstName != profile?.firstName || lastName != profile?.lastName) {
          setNameUseCase(firstName?.trim(), lastName?.trim())
        }
      }
      launch {
        val description = state.description.ifBlank { null }
        if (description != profile?.description) setDescriptionUseCase(description?.trim())
      }
    }
    _effect.emit(ProfileEditingScreenEffect.Closed)
  }

  private fun prepareCacheDir(context: Context) {
    File(getProfileCachePath(context)).mkdirs()
  }

  private fun isInputValid(nickname: String) =
    profile?.nickname == null || isNickname(nickname)

  companion object {
    private const val MAX_NICKNAME_LENGTH = 64
    private const val MAX_NAME_LENGTH = 64
    private const val MAX_DESCRIPTION_LENGTH = 150
  }
}

fun getProfileAvatarPath(context: Context): Uri =
  Uri.fromFile(File("${getProfileCachePath(context)}/avatar"))

private fun getProfileCachePath(context: Context) =
  "${context.cacheDir.absolutePath}/profile/"
