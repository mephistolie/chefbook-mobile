package io.chefbook.libs.mvi

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

abstract class BaseStore<State : Any, SideEffect : Any, Intent : Any> :
  Store<State, SideEffect, Intent> {

  protected val storeScope = CoroutineScope(Dispatchers.Main.immediate + SupervisorJob())

  override fun handle(intent: Intent) {
    storeScope.launch { reduce(intent) }
  }

  abstract suspend fun reduce(intent: Intent)

  override fun dispose() {
    storeScope.cancel()
  }
}
