package com.mysty.chefbook.api.settings.domain.usecases

import com.mysty.chefbook.api.common.entities.language.Language
import com.mysty.chefbook.api.settings.domain.ISettingsRepo

interface IGetDefaultRecipeLanguageUseCase {
    suspend operator fun invoke(): Language
}

internal class GetDefaultRecipeLanguageUseCase(
    private val settingsRepo: ISettingsRepo,
) : IGetDefaultRecipeLanguageUseCase {

    override suspend operator fun invoke() = settingsRepo.getDefaultRecipeLanguage()

}
