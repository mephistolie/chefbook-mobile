package com.cactusknights.chefbook.core.coroutines

import javax.inject.Inject
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class CoroutineScopes @Inject constructor(
    dispatchers: AppDispatchers,
) {
    companion object {
        private const val REPOSITORY_SCOPE = "REPOSITORY_SCOPE"
    }

    val repository = CoroutineScope(dispatchers.io + SupervisorJob() + CoroutineName(REPOSITORY_SCOPE))

}
