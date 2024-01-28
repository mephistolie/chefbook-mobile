package io.chefbook.sdk.settings.impl.data.sources.local

import android.content.Context
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.dataStoreFile
import io.chefbook.libs.models.language.Language
import io.chefbook.libs.models.language.LanguageMapper
import io.chefbook.sdk.settings.api.external.domain.entities.AppIcon
import io.chefbook.sdk.settings.api.external.domain.entities.ProfileMode
import io.chefbook.sdk.settings.api.external.domain.entities.AppTheme
import io.chefbook.sdk.settings.api.external.domain.entities.Environment
import io.chefbook.sdk.settings.impl.data.sources.SettingsDataSource
import io.chefbook.sdk.settings.impl.data.sources.local.datastore.SettingsSerializer
import io.chefbook.sdk.settings.impl.data.sources.local.datastore.dto.SettingsSerializable
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

internal class LocalSettingsDataSourceImpl(
  context: Context,
) : SettingsDataSource {

  private val dataStore = DataStoreFactory.create(
    serializer = SettingsSerializer,
    produceFile = { context.dataStoreFile(DATASTORE_FILE) }
  )

  override fun observeSettings() = dataStore.data.map(SettingsSerializable::toEntity)

  override suspend fun getSettings() = observeSettings().first()

  override suspend fun getProfileMode() = getSettings().profileMode

  override suspend fun setProfileMode(mode: ProfileMode) {
    dataStore.updateData { it.copy(profileMode = mode) }
  }

  override suspend fun getAppTheme() = getSettings().appTheme

  override suspend fun setAppTheme(theme: AppTheme) {
    dataStore.updateData { it.copy(appTheme = theme) }
  }

  override suspend fun getAppIcon() = getSettings().appIcon

  override suspend fun setAppIcon(icon: AppIcon) {
    dataStore.updateData { it.copy(appIcon = icon) }
  }

  override suspend fun getDefaultRecipeLanguage() =
    LanguageMapper.map(dataStore.data.first().defaultRecipeLanguage)

  override suspend fun setDefaultRecipeLanguage(language: Language) {
    dataStore.updateData { it.copy(defaultRecipeLanguage = language.code) }
  }

  override suspend fun getOnlineRecipesLanguages(): List<Language> =
    dataStore.data.first().onlineRecipeLanguages

  override suspend fun setOnlineRecipesLanguages(languages: List<Language>) {
    dataStore.updateData { it.copy(onlineRecipeLanguages = languages) }
  }

  override suspend fun getOpenSavedRecipeExpanded() = getSettings().openSavedRecipeExpanded

  override suspend fun setOpenSavedRecipeExpanded(expand: Boolean) {
    dataStore.updateData { it.copy(openSavedRecipeExpanded = expand) }
  }

  override suspend fun getEnvironment() = getSettings().environment

  override suspend fun setEnvironment(environment: Environment) {
    dataStore.updateData { it.copy(environment = environment) }
  }

  companion object {
    private const val DATASTORE_FILE = "settings.json"
  }
}
