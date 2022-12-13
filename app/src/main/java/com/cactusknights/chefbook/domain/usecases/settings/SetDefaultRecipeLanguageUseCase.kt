package com.cactusknights.chefbook.domain.usecases.settings

import com.cactusknights.chefbook.domain.entities.common.Language
import com.cactusknights.chefbook.domain.interfaces.ISettingsRepo

interface ISetDefaultRecipeLanguageUseCase {
    suspend operator fun invoke(language: Language)
}

class SetDefaultRecipeLanguageUseCase(
    private val settingsRepo: ISettingsRepo,
) : ISetDefaultRecipeLanguageUseCase {

    override suspend operator fun invoke(language: Language) = settingsRepo.setDefaultRecipeLanguage(language)

}
