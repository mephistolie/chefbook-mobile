package com.mysty.chefbook.api.settings.domain.usecases

import com.mysty.chefbook.api.common.entities.language.Language
import com.mysty.chefbook.api.settings.domain.ISettingsRepo

interface ISetDefaultRecipeLanguageUseCase {
    suspend operator fun invoke(language: Language)
}

internal class SetDefaultRecipeLanguageUseCase(
    private val settingsRepo: ISettingsRepo,
) : ISetDefaultRecipeLanguageUseCase {

    override suspend operator fun invoke(language: Language) = settingsRepo.setDefaultRecipeLanguage(language)

}
