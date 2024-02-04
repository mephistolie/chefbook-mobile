package io.chefbook.libs.mvi

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.chefbook.libs.coroutines.collectIn
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

interface StateViewModel<State : MviState> {
  val state: StateFlow<State>
}

interface IIntentViewModel<Intent : MviIntent> {
  fun handleIntent(intent: Intent)
}

interface ISideEffectViewModel<Effect : MviSideEffect> {
  val effect: Flow<Effect>
}

interface IStateEventViewModel<State : MviState, Intent : MviIntent> : StateViewModel<State>,
  IIntentViewModel<Intent>

interface IStateSideEffectViewModel<State : MviState, Effect : MviSideEffect> :
  StateViewModel<State>, ISideEffectViewModel<Effect>

interface IntentSideEffectViewModel<Intent : MviIntent, Effect : MviSideEffect> :
  IIntentViewModel<Intent>, ISideEffectViewModel<Effect>

interface MviViewModel<State : MviState, Intent : MviIntent, Effect : MviSideEffect> :
  IStateEventViewModel<State, Intent>,
  IStateSideEffectViewModel<State, Effect>,
  IntentSideEffectViewModel<Intent, Effect>

abstract class BaseStateViewModel<State : MviState> : BaseMviViewModel<State, Nothing, Nothing>()
abstract class BaseIntentViewModel<Intent : MviIntent> : BaseMviViewModel<Nothing, Intent, Nothing>()
abstract class BaseSideEffectViewModel<Effect : MviSideEffect> :
  BaseMviViewModel<Nothing, Nothing, Effect>()

abstract class BaseStateIntentViewModel<State : MviState, Intent : MviIntent> :
  BaseMviViewModel<State, Intent, Nothing>()

abstract class StateSideEffectViewModel<State : MviState, Effect : MviSideEffect> :
  BaseMviViewModel<State, Nothing, Effect>()

abstract class BaseIntentSideEffectViewModel<Intent : MviIntent, Effect : MviSideEffect> : ViewModel(),
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

abstract class BaseMviViewModel<State : MviState, Intent : MviIntent, Effect : MviSideEffect> :
  BaseIntentSideEffectViewModel<Intent, Effect>(), MviViewModel<State, Intent, Effect> {

  protected abstract val _state: MutableStateFlow<State>
  override val state: StateFlow<State> get() = _state.asStateFlow()

  fun <T> Flow<T>.collectInViewModelScope(action: suspend (T) -> Unit) =
    this.collectIn(viewModelScope, action)

  fun <T> Flow<T>.collectState(action: suspend (State, T) -> State) =
    this.collectIn(viewModelScope) { value ->
      _state.update { lastState -> action(lastState, value) }
    }
}
