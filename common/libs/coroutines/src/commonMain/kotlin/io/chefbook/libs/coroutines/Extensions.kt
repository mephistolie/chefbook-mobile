package io.chefbook.libs.coroutines

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

fun <T> Flow<T>.collectIn(scope: CoroutineScope, action: suspend (T) -> Unit) =
  this
    .onEach(action)
    .launchIn(scope)
