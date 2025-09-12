package io.chefbook.features.root.component

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import io.chefbook.features.profile.control.component.ProfileComponent
import org.koin.core.scope.Scope

interface SignedInComponent {

  val scope: Scope

  val children: Value<ChildStack<*, Child>>

  sealed interface Child {
    class Profile(val component: ProfileComponent) : Child
  }
}
