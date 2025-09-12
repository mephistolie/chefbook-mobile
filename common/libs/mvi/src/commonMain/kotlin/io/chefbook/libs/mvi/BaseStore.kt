package io.chefbook.libs.mvi

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch

abstract class BaseStore<State : Any, SideEffect : Any, Intent : Any> :
  Store<State, SideEffect, Intent> {

  protected val storeScope = CoroutineScope(Dispatchers.Main.immediate + SupervisorJob())

  override val sideEffectsFlow: MutableSharedFlow<SideEffect> =
    MutableSharedFlow<SideEffect>(extraBufferCapacity = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)

  override fun handle(intent: Intent) {
    storeScope.launch { reduce(intent) }
  }

  abstract suspend fun reduce(intent: Intent)

  override fun dispose() {
    storeScope.cancel()
  }
}
