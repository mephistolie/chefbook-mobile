package com.cactusknights.chefbook.domain.usecases.settings

import com.cactusknights.chefbook.domain.entities.settings.Settings
import com.cactusknights.chefbook.domain.interfaces.ISettingsRepo
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

interface IObserveSettingsUseCase {
    suspend operator fun invoke(): Flow<Settings>
}

class ObserveSettingsUseCase @Inject constructor(
    private val settingsRepo: ISettingsRepo,
) : IObserveSettingsUseCase {

    override suspend operator fun invoke() = settingsRepo.observeSettings()

}
