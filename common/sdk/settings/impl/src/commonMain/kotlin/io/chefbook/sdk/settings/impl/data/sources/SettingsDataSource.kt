package io.chefbook.sdk.settings.impl.data.sources

import io.chefbook.libs.models.language.Language
import io.chefbook.sdk.settings.api.external.domain.entities.AppIcon
import io.chefbook.sdk.settings.api.external.domain.entities.AppTheme
import io.chefbook.sdk.settings.api.external.domain.entities.Environment
import io.chefbook.sdk.settings.api.external.domain.entities.ProfileMode
import io.chefbook.sdk.settings.api.external.domain.entities.Settings
import kotlinx.coroutines.flow.Flow

interface SettingsDataSource {
  fun observeSettings(): Flow<Settings>

  suspend fun getSettings(): Settings

  suspend fun getProfileMode(): ProfileMode

  suspend fun setProfileMode(mode: ProfileMode)

  suspend fun getAppTheme(): AppTheme

  suspend fun setAppTheme(theme: AppTheme)

  suspend fun getAppIcon(): AppIcon

  suspend fun setAppIcon(icon: AppIcon)

  suspend fun getDefaultRecipeLanguage(): Language

  suspend fun setDefaultRecipeLanguage(language: Language)

  fun observeCommunityRecipesLanguages(): Flow<List<Language>>

  suspend fun getCommunityRecipesLanguages(): List<Language>

  suspend fun setCommunityRecipesLanguages(languages: List<Language>)

  suspend fun getOpenSavedRecipeExpanded(): Boolean

  suspend fun setOpenSavedRecipeExpanded(expand: Boolean)

  suspend fun getEnvironment(): Environment

  suspend fun setEnvironment(environment: Environment)
}
