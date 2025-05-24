package io.chefbook.sdk.auth.api.external.domain.usecases

import kotlinx.coroutines.flow.Flow

interface ObserveCurrentProfileIdUseCase {

  operator fun invoke(): Flow<String?>
}
