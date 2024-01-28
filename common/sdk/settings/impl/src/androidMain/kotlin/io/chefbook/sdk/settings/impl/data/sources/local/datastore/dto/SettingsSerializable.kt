package io.chefbook.sdk.settings.impl.data.sources.local.datastore.dto

import io.chefbook.libs.models.language.Language
import io.chefbook.sdk.settings.api.external.domain.entities.AppIcon
import io.chefbook.sdk.settings.api.external.domain.entities.AppTheme
import io.chefbook.sdk.settings.api.external.domain.entities.Environment
import io.chefbook.sdk.settings.api.external.domain.entities.ProfileMode
import io.chefbook.sdk.settings.api.external.domain.entities.Settings
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class SettingsSerializable(
  @SerialName("profileMode")
  val profileMode: ProfileMode = ProfileMode.ONLINE,
  @SerialName("appIcon")
  val appIcon: AppIcon = AppIcon.STANDARD,
  @SerialName("appTheme")
  val appTheme: AppTheme = AppTheme.SYSTEM,
  @SerialName("openSavedRecipeExpanded")
  val openSavedRecipeExpanded: Boolean = false,

  @SerialName("environment")
  val environment: Environment = Environment.DEVELOP,

  @SerialName("defaultRecipeLanguage")
  val defaultRecipeLanguage: String = Language.ENGLISH.code,
  @SerialName("onlineRecipeLanguage")
  val onlineRecipeLanguages: List<Language> = emptyList(),
) {

  fun toEntity(): Settings =
    Settings(
      profileMode = profileMode,
      appIcon = appIcon,
      appTheme = appTheme,

      openSavedRecipeExpanded = openSavedRecipeExpanded,

      environment = environment,
    )
}
