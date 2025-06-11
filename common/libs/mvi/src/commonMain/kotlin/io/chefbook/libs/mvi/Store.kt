package io.chefbook.libs.mvi

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface Store<State : Any, SideEffect : Any, Intent : Any> {

  val stateFlow: StateFlow<State>

  val sideEffectsFlow: Flow<SideEffect>

  fun handle(intent: Intent)

  fun dispose()
}
