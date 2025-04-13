package io.chefbook.libs.di.scopes

import org.koin.core.Koin
import org.koin.core.parameter.parametersOf
import org.koin.core.scope.Scope

object ProfileScope {
  const val QUALIFIER = "profile_scope"

  const val PARAM_PROFILE_ID = 0
}

fun Koin.getOrCreateProfileScope(profileId: String): Scope {
  return getOrCreateScope<ProfileScope>(
    scopeId = "profile_$profileId",
  ).get {
    parametersOf(profileId)
  }
}
