package io.chefbook.features.auth.di

import io.chefbook.features.auth.profiles.mvi.ProfilesListStore
import io.chefbook.features.auth.profiles.mvi.ProfilesListStoreImpl
import io.chefbook.features.auth.signin.login.mvi.SignInLoginStore
import io.chefbook.features.auth.signin.login.mvi.SignInLoginStoreImpl
import io.chefbook.features.auth.signin.password.mvi.SignInPasswordStore
import io.chefbook.features.auth.signin.password.mvi.SignInPasswordStoreImpl
import io.chefbook.features.auth.signup.activation.mvi.ProfileActivationStore
import io.chefbook.features.auth.signup.activation.mvi.ProfileActivationStoreImpl
import io.chefbook.features.auth.signup.email.mvi.SignUpEmailStore
import io.chefbook.features.auth.signup.email.mvi.SignUpEmailStoreImpl
import io.chefbook.features.auth.signup.password.mvi.SignUpPasswordStore
import io.chefbook.features.auth.signup.password.mvi.SignUpPasswordStoreImpl
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module

fun featureAuthModule() = module {
  factoryOf(::ProfilesListStoreImpl) bind ProfilesListStore::class
  factoryOf(::SignInLoginStoreImpl) bind SignInLoginStore::class
  factoryOf(::SignInPasswordStoreImpl) bind SignInPasswordStore::class
  factoryOf(::SignUpEmailStoreImpl) bind SignUpEmailStore::class
  factoryOf(::SignUpPasswordStoreImpl) bind SignUpPasswordStore::class
  factoryOf(::ProfileActivationStoreImpl) bind ProfileActivationStore::class
}
