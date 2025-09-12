package io.chefbook.features.root.component

import com.arkivanov.decompose.router.slot.ChildSlot
import com.arkivanov.decompose.value.Value
import io.chefbook.features.auth.AuthComponent
import io.chefbook.features.root.ui.mvi.RootStore

interface RootComponent {

  val store: RootStore

  val child: Value<ChildSlot<*, Child>>

  fun onSignedIn(profileId: String)

  fun onSignedOut()

  sealed interface Child {
    class SignedOut(val component: AuthComponent) : Child
    class SignedIn(val component: SignedInComponent) : Child

    sealed interface Modal : Child
  }
}
