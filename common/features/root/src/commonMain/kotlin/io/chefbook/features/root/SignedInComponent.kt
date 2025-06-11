package io.chefbook.features.root

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import io.chefbook.features.profile.ProfileComponent
import org.koin.core.scope.Scope

interface SignedInComponent {

  val scope: Scope

  val children: Value<ChildStack<*, Child>>

  sealed interface Child {
    class Profile(val component: ProfileComponent) : Child
  }
}
