package io.chefbook.libs.di.scopes

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import org.koin.core.component.KoinScopeComponent
import org.koin.core.component.createScope
import org.koin.core.scope.Scope
import org.koin.core.scope.ScopeCallback

class ProfileComponent(
  val profileId: String,
) : KoinScopeComponent {

  override val scope: Scope by lazy { createScope(this) }

  val coroutineScope: CoroutineScope by lazy {
    CoroutineScope(SupervisorJob()).also { instance ->
      scope.registerCallback(
        object : ScopeCallback {
          override fun onScopeClose(scope: Scope) = instance.cancel()
        }
      )
    }
  }

  fun close() {
    if (scope.isNotClosed()) {
      scope.close()
    }
  }
}
