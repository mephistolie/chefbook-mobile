package com.cactusknights.chefbook.common

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

open class BaseViewModel<T>(initialState: T): ViewModel() {

    protected val _state = MutableStateFlow(initialState)
    val state: StateFlow<T> = _state

    fun restoreState(state: T) { viewModelScope.launch { _state.emit(state) } }
}