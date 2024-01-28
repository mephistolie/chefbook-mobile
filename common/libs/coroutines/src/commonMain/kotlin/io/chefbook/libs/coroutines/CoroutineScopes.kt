package io.chefbook.libs.coroutines

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class CoroutineScopes(
  dispatchers: AppDispatchers,
) {

  val repository =
    CoroutineScope(dispatchers.io + SupervisorJob())
}
