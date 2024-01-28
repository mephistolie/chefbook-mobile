package io.chefbook.sdk.settings.impl.domain.usecases

import io.chefbook.sdk.core.api.internal.data.repositories.LocalDataRepository
import io.chefbook.sdk.settings.api.external.domain.entities.Environment
import io.chefbook.sdk.settings.api.external.domain.usecases.SetEnvironmentUseCase
import io.chefbook.sdk.settings.api.internal.data.repositories.SettingsRepository

internal class SetEnvironmentUseCaseImpl(
  private val localDataRepository: LocalDataRepository,
  private val settingsRepository: SettingsRepository,
) : SetEnvironmentUseCase {

  override suspend operator fun invoke(environment: Environment) {
    localDataRepository.clearLocalData()
    settingsRepository.setEnvironment(environment)
  }
}
