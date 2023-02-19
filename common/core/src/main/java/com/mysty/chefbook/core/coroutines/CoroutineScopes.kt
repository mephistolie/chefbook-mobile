package com.mysty.chefbook.core.coroutines

import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class CoroutineScopes (
    dispatchers: AppDispatchers,
) {
    companion object {
        private const val REPOSITORY_SCOPE = "REPOSITORY_SCOPE"
    }

    val repository = CoroutineScope(dispatchers.io + SupervisorJob() + CoroutineName(REPOSITORY_SCOPE))

}
