package com.cactusknights.chefbook.domain.usecases.profile

import com.cactusknights.chefbook.domain.entities.profile.Profile
import com.cactusknights.chefbook.domain.interfaces.IProfileRepo
import kotlinx.coroutines.flow.StateFlow

interface IObserveProfileUseCase {
    suspend operator fun invoke(): StateFlow<Profile?>
}

class ObserveProfileUseCase(
    private val profileRepo: IProfileRepo,
) : IObserveProfileUseCase {

    override suspend operator fun invoke() = profileRepo.observeProfile()

}
