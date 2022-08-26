package com.cactusknights.chefbook.domain.usecases.settings

import com.cactusknights.chefbook.domain.entities.common.Language
import com.cactusknights.chefbook.domain.interfaces.ISettingsRepo
import javax.inject.Inject

interface ISetDefaultRecipeLanguageUseCase {
    suspend operator fun invoke(language: Language)
}

class SetDefaultRecipeLanguageUseCase @Inject constructor(
    private val settingsRepo: ISettingsRepo,
) : ISetDefaultRecipeLanguageUseCase {

    override suspend operator fun invoke(language: Language) = settingsRepo.setDefaultRecipeLanguage(language)

}
