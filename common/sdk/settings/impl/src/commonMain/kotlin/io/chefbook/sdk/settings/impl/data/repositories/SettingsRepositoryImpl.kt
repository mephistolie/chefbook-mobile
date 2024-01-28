package io.chefbook.sdk.settings.impl.data.repositories

import io.chefbook.libs.models.language.Language
import io.chefbook.sdk.settings.api.external.domain.entities.AppIcon
import io.chefbook.sdk.settings.api.external.domain.entities.AppTheme
import io.chefbook.sdk.settings.api.external.domain.entities.Environment
import io.chefbook.sdk.settings.api.external.domain.entities.ProfileMode
import io.chefbook.sdk.settings.api.internal.data.repositories.SettingsRepository
import io.chefbook.sdk.settings.impl.data.sources.SettingsDataSource

internal class SettingsRepositoryImpl(
  private val localDataSource: SettingsDataSource
) : SettingsRepository {
  
  private var lastSetIcon: AppIcon? = null

  override fun observeSettings() = localDataSource.observeSettings()

  override suspend fun getSettings() = localDataSource.getSettings()

  override suspend fun getProfileMode() = localDataSource.getProfileMode()

  override suspend fun setProfileMode(mode: ProfileMode) = localDataSource.setProfileMode(mode)

  override suspend fun getAppTheme() = localDataSource.getAppTheme()

  override suspend fun setAppTheme(theme: AppTheme) = localDataSource.setAppTheme(theme)

  override suspend fun getAppIcon() = localDataSource.getAppIcon()

  override suspend fun setAppIcon(icon: AppIcon) {
    localDataSource.setAppIcon(icon)
    lastSetIcon = icon
  }

  override suspend fun getDefaultRecipeLanguage() = localDataSource.getDefaultRecipeLanguage()

  override suspend fun setDefaultRecipeLanguage(language: Language) =
    localDataSource.setDefaultRecipeLanguage(language)

  override suspend fun getOnlineRecipesLanguages() = localDataSource.getOnlineRecipesLanguages()

  override suspend fun setOnlineRecipesLanguages(languages: List<Language>) =
    localDataSource.setOnlineRecipesLanguages(languages)

  override suspend fun getOpenSavedRecipeExpanded() = localDataSource.getOpenSavedRecipeExpanded()

  override suspend fun setOpenSavedRecipeExpanded(expand: Boolean) =
    localDataSource.setOpenSavedRecipeExpanded(expand)

  override suspend fun getEnvironment() = localDataSource.getEnvironment()

  override suspend fun setEnvironment(environment: Environment) = localDataSource.setEnvironment(environment)

  override fun lastSetIcon() = lastSetIcon
}
