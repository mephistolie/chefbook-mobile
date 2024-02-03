package io.chefbook.features.auth.form.di

import io.chefbook.features.auth.form.data.oauth.GoogleAuthenticator
import io.chefbook.features.auth.form.data.oauth.GoogleAuthenticatorImpl
import io.chefbook.features.auth.form.ui.AuthViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val featureAuthModule = module {

  singleOf(::GoogleAuthenticatorImpl) bind GoogleAuthenticator::class

  viewModelOf(::AuthViewModel)
}
