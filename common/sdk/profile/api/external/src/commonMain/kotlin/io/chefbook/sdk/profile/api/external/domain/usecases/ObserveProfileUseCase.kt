package io.chefbook.sdk.profile.api.external.domain.usecases

import io.chefbook.sdk.profile.api.external.domain.entities.Profile
import kotlinx.coroutines.flow.Flow

interface ObserveProfileUseCase {
  operator fun invoke(): Flow<Profile?>
}