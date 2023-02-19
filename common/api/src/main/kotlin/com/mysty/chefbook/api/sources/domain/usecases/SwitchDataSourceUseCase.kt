package com.mysty.chefbook.api.sources.domain.usecases

import com.mysty.chefbook.api.sources.domain.ISourcesRepo

interface ISwitchDataSourceUseCase {
    suspend operator fun invoke(useRemoteSource: Boolean)
}

internal class SwitchDataSourceUseCase(
    private val sourceRepo: ISourcesRepo,
): ISwitchDataSourceUseCase {

    override suspend fun invoke(useRemoteSource: Boolean) = sourceRepo.setServerAccess(useRemoteSource)

}
