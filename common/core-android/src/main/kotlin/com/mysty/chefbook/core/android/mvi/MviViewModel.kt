package com.mysty.chefbook.core.android.mvi

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

interface IStateViewModel<State : MviState> {
    val state: StateFlow<State>
}

interface IIntentViewModel<Intent : MviIntent> {
    fun handleIntent(intent: Intent)
}

interface ISideEffectViewModel<Effect : MviSideEffect> {
    val effect: SharedFlow<Effect>
}

interface IStateEventViewModel<State : MviState, Intent : MviIntent> : IStateViewModel<State>, IIntentViewModel<Intent>
interface IStateSideEffectViewModel<State : MviState, Effect : MviSideEffect> : IStateViewModel<State>, ISideEffectViewModel<Effect>
interface IIntentSideEffectViewModel<Intent : MviIntent, Effect : MviSideEffect> : IIntentViewModel<Intent>, ISideEffectViewModel<Effect>

interface IMviViewModel<State : MviState, Intent : MviIntent, Effect : MviSideEffect> :
    IStateEventViewModel<State, Intent>,
    IStateSideEffectViewModel<State, Effect>,
    IIntentSideEffectViewModel<Intent, Effect>

abstract class StateViewModel<State : MviState> : MviViewModel<State, Nothing, Nothing>()
abstract class IntentViewModel<Intent : MviIntent> : MviViewModel<Nothing, Intent, Nothing>()
abstract class SideEffectViewModel<Effect : MviSideEffect> : MviViewModel<Nothing, Nothing, Effect>()

abstract class StateEventViewModel<State : MviState, Intent : MviIntent> : MviViewModel<State, Intent, Nothing>()
abstract class StateSideEffectViewModel<State : MviState, Effect : MviSideEffect> : MviViewModel<State, Nothing, Effect>()
abstract class IntentSideEffectViewModel<Intent : MviIntent, Effect : MviSideEffect> : ViewModel(), IIntentSideEffectViewModel<Intent, Effect> {

    protected val _effect: MutableSharedFlow<Effect> = MutableSharedFlow()
    override val effect = _effect.asSharedFlow()

    override fun handleIntent(intent: Intent) {
        viewModelScope.launch {
            reduceIntent(intent)
        }
    }

    protected open suspend fun reduceIntent(intent: Intent) = Unit
}

abstract class MviViewModel<State : MviState, Intent : MviIntent, Effect : MviSideEffect> : IntentSideEffectViewModel<Intent, Effect>(), IMviViewModel<State, Intent, Effect> {

    protected abstract val _state: MutableStateFlow<State>
    override val state get() = _state.asStateFlow()

    private val mutex = Mutex()

    protected suspend fun withSafeState(
        block: suspend (State) -> Unit
    ) {
        mutex.withLock { block(state.value) }
    }

    protected suspend fun updateStateSafely(
        block: suspend (State) -> State
    ) {
        withSafeState {
            val newState = block(state.value)
            _state.emit(newState)
        }
    }
}
