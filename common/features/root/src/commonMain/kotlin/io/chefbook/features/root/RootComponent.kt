package io.chefbook.features.root

import com.arkivanov.decompose.router.slot.ChildSlot
import com.arkivanov.decompose.value.Value
import io.chefbook.features.auth.AuthComponent

interface RootComponent {

  val child: Value<ChildSlot<*, Child>>

  fun onSignedIn(profileId: String)

  fun onSignedOut()

  sealed interface Child {
    class SignedOut(val component: AuthComponent) : Child
    class SignedIn(val component: SignedInComponent) : Child

    sealed interface Modal : Child
  }
}
