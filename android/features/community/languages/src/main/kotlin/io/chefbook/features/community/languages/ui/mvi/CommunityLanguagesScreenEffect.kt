package io.chefbook.features.community.languages.ui.mvi

import io.chefbook.libs.mvi.MviSideEffect

internal sealed interface CommunityLanguagesScreenEffect : MviSideEffect {

  data object Back : CommunityLanguagesScreenEffect
}
