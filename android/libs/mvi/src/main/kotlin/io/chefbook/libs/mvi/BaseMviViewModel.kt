package io.chefbook.libs.mvi

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.chefbook.libs.coroutines.collectIn
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

interface StateViewModel<State : Any> {
  val state: StateFlow<State>
}

interface IIntentViewModel<Intent : Any> {
  fun handleIntent(intent: Intent)
}

interface ISideEffectViewModel<Effect : Any> {
  val effect: Flow<Effect>
}

interface IStateEventViewModel<State : Any, Intent : Any> : StateViewModel<State>,
  IIntentViewModel<Intent>

interface IStateSideEffectViewModel<State : Any, Effect : Any> :
  StateViewModel<State>, ISideEffectViewModel<Effect>

interface IntentSideEffectViewModel<Intent : Any, Effect : Any> :
  IIntentViewModel<Intent>, ISideEffectViewModel<Effect>

interface MviViewModel<State : Any, Intent : Any, Effect : Any> :
  IStateEventViewModel<State, Intent>,
  IStateSideEffectViewModel<State, Effect>,
  IntentSideEffectViewModel<Intent, Effect>

abstract class BaseStateViewModel<State : Any> : BaseMviViewModel<State, Nothing, Nothing>()
abstract class BaseIntentViewModel<Intent : Any> : BaseMviViewModel<Nothing, Intent, Nothing>()
abstract class BaseSideEffectViewModel<Effect : Any> :
  BaseMviViewModel<Nothing, Nothing, Effect>()

abstract class BaseStateIntentViewModel<State : Any, Intent : Any> :
  BaseMviViewModel<State, Intent, Nothing>()

abstract class StateSideEffectViewModel<State : Any, Effect : Any> :
  BaseMviViewModel<State, Nothing, Effect>()

abstract class BaseIntentSideEffectViewModel<Intent : Any, Effect : Any> : ViewModel(),
  IntentSideEffectViewModel<Intent, Effect> {

  protected val _effect: MutableSharedFlow<Effect> = MutableSharedFlow()
  override val effect: Flow<Effect> = _effect.asSharedFlow()

  override fun handleIntent(intent: Intent) {
    viewModelScope.launch {
      reduceIntent(intent)
    }
  }

  protected open suspend fun reduceIntent(intent: Intent) = Unit
}

abstract class BaseMviViewModel<State : Any, Intent : Any, Effect : Any> :
  BaseIntentSideEffectViewModel<Intent, Effect>(), MviViewModel<State, Intent, Effect> {

  protected abstract val _state: MutableStateFlow<State>
  override val state: StateFlow<State> get() = _state.asStateFlow()

  protected fun <T> Flow<T>.collectInViewModelScope(action: suspend (T) -> Unit) =
    this.collectIn(viewModelScope, action)

  protected fun <T> Flow<T>.collectState(action: suspend (State, T) -> State) =
    this.collectIn(viewModelScope) { value ->
      _state.update { lastState -> action(lastState, value) }
    }
}
