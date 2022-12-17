package com.mysty.chefbook.api.settings.domain.usecases

import com.mysty.chefbook.api.settings.domain.ISettingsRepo
import com.mysty.chefbook.api.settings.domain.entities.Settings
import kotlinx.coroutines.flow.Flow

interface IObserveSettingsUseCase {
    suspend operator fun invoke(): Flow<Settings>
}

internal class ObserveSettingsUseCase(
    private val settingsRepo: ISettingsRepo,
) : IObserveSettingsUseCase {

    override suspend operator fun invoke() = settingsRepo.observeSettings()

}
