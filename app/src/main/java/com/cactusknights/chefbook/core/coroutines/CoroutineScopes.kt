package com.cactusknights.chefbook.core.coroutines

import javax.inject.Inject
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope

class CoroutineScopes @Inject constructor(
    dispatchers: AppDispatchers,
) {
    companion object {
        private const val REPOSITORY_SCOPE = "REPOSITORY_SCOPE"
    }

    val repository = CoroutineScope(dispatchers.io + CoroutineName(REPOSITORY_SCOPE))
}
