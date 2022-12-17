package com.mysty.chefbook.api.profile.domain.usecases

import com.mysty.chefbook.api.profile.domain.IProfileRepo
import com.mysty.chefbook.api.profile.domain.entities.Profile
import kotlinx.coroutines.flow.StateFlow

interface IObserveProfileUseCase {
    suspend operator fun invoke(): StateFlow<Profile?>
}

internal class ObserveProfileUseCase(
    private val profileRepo: IProfileRepo,
) : IObserveProfileUseCase {

    override suspend operator fun invoke() = profileRepo.observeProfile()

}
