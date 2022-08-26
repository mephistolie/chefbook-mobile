package com.cactusknights.chefbook.data.dto

import com.cactusknights.chefbook.SettingsProto
import com.cactusknights.chefbook.domain.entities.settings.Icon
import com.cactusknights.chefbook.domain.entities.settings.Mode
import com.cactusknights.chefbook.domain.entities.settings.Settings
import com.cactusknights.chefbook.domain.entities.settings.Tab
import com.cactusknights.chefbook.domain.entities.settings.Theme

fun SettingsProto.toEntity(): Settings =
    Settings(
        mode = appMode.toEntity(),
        icon = appIcon.toEntity(),
        theme = appTheme.toEntity(),
        defaultTab = defaultTab.toEntity(),
        isFirstLaunch = isFirstAppLaunch,
        defaultRecipeLanguage = defaultRecipeLanguage,
        onlineRecipeLanguages = onlineRecipesLanguagesList,
    )

fun SettingsProto.AppMode.toEntity(): Mode =
    when (this) {
        SettingsProto.AppMode.OFFLINE -> Mode.OFFLINE
        else -> Mode.ONLINE
    }

fun Mode.toProto(): SettingsProto.AppMode =
    when (this) {
        Mode.OFFLINE -> SettingsProto.AppMode.OFFLINE
        else -> SettingsProto.AppMode.ONLINE
    }

fun SettingsProto.AppTheme.toEntity(): Theme =
    when (this) {
        SettingsProto.AppTheme.LIGHT -> Theme.LIGHT
        SettingsProto.AppTheme.DARK -> Theme.DARK
        else -> Theme.SYSTEM
    }

fun Theme.toProto(): SettingsProto.AppTheme =
    when (this) {
        Theme.LIGHT -> SettingsProto.AppTheme.LIGHT
        Theme.DARK -> SettingsProto.AppTheme.DARK
        else -> SettingsProto.AppTheme.SYSTEM
    }

fun SettingsProto.AppIcon.toEntity(): Icon =
    when (this) {
        SettingsProto.AppIcon.OLD -> Icon.OLD
        else -> Icon.STANDARD
    }

fun Icon.toProto(): SettingsProto.AppIcon =
    when (this) {
        Icon.OLD -> SettingsProto.AppIcon.OLD
        else -> SettingsProto.AppIcon.STANDARD
    }

fun SettingsProto.Tab.toEntity(): Tab =
    when (this) {
        SettingsProto.Tab.RECIPE_BOOK -> Tab.RECIPE_BOOK
        else -> Tab.SHOPPING_LIST
    }

fun Tab.toProto(): SettingsProto.Tab =
    when (this) {
        Tab.RECIPE_BOOK -> SettingsProto.Tab.RECIPE_BOOK
        else -> SettingsProto.Tab.SHOPPING_LIST
    }
