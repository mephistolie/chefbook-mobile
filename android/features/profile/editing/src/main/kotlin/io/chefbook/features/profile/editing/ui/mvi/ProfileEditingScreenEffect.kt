package io.chefbook.features.profile.editing.ui.mvi

import io.chefbook.libs.mvi.MviSideEffect

internal sealed interface ProfileEditingScreenEffect : MviSideEffect {

  data object ProfileDeletionScreenOpened : ProfileEditingScreenEffect

  data object Closed : ProfileEditingScreenEffect
}
