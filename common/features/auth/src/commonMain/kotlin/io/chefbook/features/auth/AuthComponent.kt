package io.chefbook.features.auth

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.instancekeeper.InstanceKeeperOwner
import io.chefbook.features.auth.login.component.LoginFormComponent
import io.chefbook.features.auth.profiles.component.ProfilesListComponent


interface AuthComponent : InstanceKeeperOwner {

//  val children: Value<ChildStack<*, Child>>

  sealed interface Child {

    data class ProfilesList(val component: ProfilesListComponent) : Child

    data class Login(val component: LoginFormComponent) : Child

    data class Password(val login: String) : Child

    data object PasswordResetForm : Child

    data object PasswordResetConfirmation : Child
  }
}
