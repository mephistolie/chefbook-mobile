package io.chefbook.sdk.settings.api.external.domain.entities

data class Settings(
  val profileMode: ProfileMode = ProfileMode.ONLINE,
  val appIcon: AppIcon = AppIcon.STANDARD,
  val appTheme: AppTheme = AppTheme.SYSTEM,
  val openSavedRecipeExpanded: Boolean = false,

  val environment: Environment = Environment.DEVELOP,
)
