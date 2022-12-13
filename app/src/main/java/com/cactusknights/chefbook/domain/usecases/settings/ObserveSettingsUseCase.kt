package com.cactusknights.chefbook.domain.usecases.settings

import com.cactusknights.chefbook.domain.entities.settings.Settings
import com.cactusknights.chefbook.domain.interfaces.ISettingsRepo
import kotlinx.coroutines.flow.Flow

interface IObserveSettingsUseCase {
    suspend operator fun invoke(): Flow<Settings>
}

class ObserveSettingsUseCase(
    private val settingsRepo: ISettingsRepo,
) : IObserveSettingsUseCase {

    override suspend operator fun invoke() = settingsRepo.observeSettings()

}
