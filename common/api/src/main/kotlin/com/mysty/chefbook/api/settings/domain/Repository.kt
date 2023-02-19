package com.mysty.chefbook.api.settings.domain

import com.mysty.chefbook.api.common.entities.language.Language
import com.mysty.chefbook.api.settings.domain.entities.Icon
import com.mysty.chefbook.api.settings.domain.entities.Mode
import com.mysty.chefbook.api.settings.domain.entities.Settings
import com.mysty.chefbook.api.settings.domain.entities.Tab
import com.mysty.chefbook.api.settings.domain.entities.Theme
import kotlinx.coroutines.flow.Flow

internal interface ISettingsRepo {
    fun observeSettings(): Flow<Settings>
    suspend fun getSettings(): Settings
    suspend fun getAppMode(): Mode
    suspend fun setAppMode(mode: Mode)
    suspend fun getAppTheme(): Theme
    suspend fun setAppTheme(theme: Theme)
    suspend fun getAppIcon(): Icon
    suspend fun setAppIcon(icon: Icon)
    suspend fun getDefaultTab(): Tab
    suspend fun setDefaultTab(tab: Tab)
    suspend fun checkFirstAppLaunch(): Boolean
    suspend fun getDefaultRecipeLanguage(): Language
    suspend fun setDefaultRecipeLanguage(language: Language)
    suspend fun getOnlineRecipesLanguages(): List<String>
    suspend fun setOnlineRecipesLanguages(languages: List<String>)
}
