package io.chefbook.features.profile.deletion.ui.mvi

import io.chefbook.libs.mvi.MviSideEffect

internal sealed interface ProfileDeletionScreenEffect : MviSideEffect {

  data class ToastShown(val message: String) : ProfileDeletionScreenEffect

  data object Closed : ProfileDeletionScreenEffect
}
