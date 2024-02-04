package io.chefbook.features.profile.editing.ui

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.chefbook.features.profile.editing.ui.components.AvatarEditing
import io.chefbook.features.profile.editing.ui.components.DescriptionInputBlock
import io.chefbook.features.profile.editing.ui.components.NameInputBlock
import io.chefbook.features.profile.editing.ui.components.NicknameInputBlock
import io.chefbook.features.profile.editing.ui.components.ProfileEditingScreenToolbar
import io.chefbook.features.profile.editing.ui.mvi.ProfileEditingScreenIntent
import io.chefbook.features.profile.editing.ui.mvi.ProfileEditingScreenState
import io.chefbook.ui.common.components.menu.MenuDivider
import io.chefbook.ui.common.components.menu.MenuGroup
import io.chefbook.ui.common.components.menu.MenuScreen

@Composable
internal fun ProfileEditingScreenContent(
  state: ProfileEditingScreenState,
  onIntent: (ProfileEditingScreenIntent) -> Unit,
) {
  MenuScreen(
    toolbar = {
      ProfileEditingScreenToolbar(
        isConfirmButtonAvailable = state.isConfirmButtonAvailable,
        onBackClick = { onIntent(ProfileEditingScreenIntent.Close) },
        onConfirmClick = { onIntent(ProfileEditingScreenIntent.Confirm) }
      )
    },
  ) {
    MenuGroup(isFirst = true) {
      Spacer(modifier = Modifier.height(20.dp))
      AvatarEditing(
        avatar = state.avatar,
        onAvatarSet = { onIntent(ProfileEditingScreenIntent.SetAvatar(it)) },
        onDeleteClick = { onIntent(ProfileEditingScreenIntent.DeleteAvatar) },
      )
      Spacer(modifier = Modifier.height(36.dp))
      NicknameInputBlock(
        nickname = state.nickname,
        onNicknameChange = { text -> onIntent(ProfileEditingScreenIntent.SetNickname(text)) },
        isValid = state.nicknameValid,
        hint = state.nicknameHint,
      )
      Spacer(modifier = Modifier.height(12.dp))
    }
    MenuDivider()
    MenuGroup(isLast = true) {
      Spacer(modifier = Modifier.height(24.dp))
      NameInputBlock(
        firstName = state.firstName,
        onFirstNameChange = { onIntent(ProfileEditingScreenIntent.SetFirstName(it)) },
        lastName = state.lastName,
        onLastNameChange = { onIntent(ProfileEditingScreenIntent.SetLastName(it)) },
      )
      Spacer(modifier = Modifier.height(24.dp))
      DescriptionInputBlock(
        description = state.description,
        onDescriptionChange = { onIntent(ProfileEditingScreenIntent.SetDescription(it)) },
      )
    }
  }
}
