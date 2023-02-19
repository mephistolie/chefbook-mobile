package com.mysty.chefbook.api.settings.data.local.mappers

import com.mysty.chefbook.api.settings.data.local.dto.SettingsProto
import com.mysty.chefbook.api.settings.domain.entities.Icon
import com.mysty.chefbook.api.settings.domain.entities.Mode
import com.mysty.chefbook.api.settings.domain.entities.Settings
import com.mysty.chefbook.api.settings.domain.entities.Tab
import com.mysty.chefbook.api.settings.domain.entities.Theme

internal fun SettingsProto.toEntity(): Settings =
    Settings(
        mode = appMode.toEntity(),
        icon = appIcon.toEntity(),
        theme = appTheme.toEntity(),
        defaultTab = defaultTab.toEntity(),
        isFirstLaunch = isFirstAppLaunch,
        defaultRecipeLanguage = defaultRecipeLanguage,
        onlineRecipeLanguages = onlineRecipesLanguagesList,
    )

internal fun SettingsProto.AppMode.toEntity(): Mode =
    when (this) {
        SettingsProto.AppMode.OFFLINE -> Mode.OFFLINE
        else -> Mode.ONLINE
    }

internal fun Mode.toProto(): SettingsProto.AppMode =
    when (this) {
        Mode.OFFLINE -> SettingsProto.AppMode.OFFLINE
        else -> SettingsProto.AppMode.ONLINE
    }

internal fun SettingsProto.AppTheme.toEntity(): Theme =
    when (this) {
        SettingsProto.AppTheme.LIGHT -> Theme.LIGHT
        SettingsProto.AppTheme.DARK -> Theme.DARK
        else -> Theme.SYSTEM
    }

internal fun Theme.toProto(): SettingsProto.AppTheme =
    when (this) {
        Theme.LIGHT -> SettingsProto.AppTheme.LIGHT
        Theme.DARK -> SettingsProto.AppTheme.DARK
        else -> SettingsProto.AppTheme.SYSTEM
    }

internal fun SettingsProto.AppIcon.toEntity(): Icon =
    when (this) {
        SettingsProto.AppIcon.OLD -> Icon.OLD
        else -> Icon.STANDARD
    }

internal fun Icon.toProto(): SettingsProto.AppIcon =
    when (this) {
        Icon.OLD -> SettingsProto.AppIcon.OLD
        else -> SettingsProto.AppIcon.STANDARD
    }

internal fun SettingsProto.Tab.toEntity(): Tab =
    when (this) {
        SettingsProto.Tab.RECIPE_BOOK -> Tab.RECIPE_BOOK
        else -> Tab.SHOPPING_LIST
    }

internal fun Tab.toProto(): SettingsProto.Tab =
    when (this) {
        Tab.RECIPE_BOOK -> SettingsProto.Tab.RECIPE_BOOK
        else -> SettingsProto.Tab.SHOPPING_LIST
    }
