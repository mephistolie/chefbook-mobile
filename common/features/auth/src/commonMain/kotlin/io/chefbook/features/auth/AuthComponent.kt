package io.chefbook.features.auth

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.instancekeeper.InstanceKeeperOwner
import io.chefbook.features.auth.profiles.component.ProfilesListComponent
import io.chefbook.features.auth.signin.login.component.SignInLoginComponent
import io.chefbook.features.auth.signin.password.component.SignInPasswordComponent
import io.chefbook.features.auth.signup.activation.component.ProfileActivationComponent
import io.chefbook.features.auth.signup.email.component.SignUpEmailComponent
import io.chefbook.features.auth.signup.password.component.SignUpPasswordComponent

interface AuthComponent : InstanceKeeperOwner {

  val children: Value<ChildStack<*, Child>>

  sealed interface Child {

    data class ProfilesList(val component: ProfilesListComponent) : Child

    data class SignInLogin(val component: SignInLoginComponent) : Child

    data class SignInPassword(val component: SignInPasswordComponent) : Child

    data class SignUpEmail(val component: SignUpEmailComponent) : Child

    data class SignUpPassword(val component: SignUpPasswordComponent) : Child

    data class ProfileActivation(val component: ProfileActivationComponent) : Child

    data object PasswordResetForm : Child

    data object PasswordResetConfirmation : Child
  }
}
