package io.chefbook.libs.coroutines

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.MainCoroutineDispatcher

class AppDispatchers(
  val main: MainCoroutineDispatcher = Dispatchers.Main,
  val computation: CoroutineDispatcher = Dispatchers.Default,
  val io: CoroutineDispatcher = Dispatchers.IO,
  val unconfined: CoroutineDispatcher = Dispatchers.Unconfined,
)
