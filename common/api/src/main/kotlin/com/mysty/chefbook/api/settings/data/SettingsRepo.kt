package com.mysty.chefbook.api.settings.data

import androidx.datastore.core.DataStore
import com.mysty.chefbook.api.common.entities.language.Language
import com.mysty.chefbook.api.common.entities.language.LanguageMapper
import com.mysty.chefbook.api.settings.data.local.dto.SettingsProto
import com.mysty.chefbook.api.settings.data.local.mappers.toEntity
import com.mysty.chefbook.api.settings.data.local.mappers.toProto
import com.mysty.chefbook.api.settings.domain.ISettingsRepo
import com.mysty.chefbook.api.settings.domain.entities.Icon
import com.mysty.chefbook.api.settings.domain.entities.Mode
import com.mysty.chefbook.api.settings.domain.entities.Tab
import com.mysty.chefbook.api.settings.domain.entities.Theme
import java.io.IOException
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.take

internal class SettingsRepo(
    private val settings: DataStore<SettingsProto>
): ISettingsRepo {

    private val saved get() = settings.data.take(1)

    override fun observeSettings() = settings.data.map { it.toEntity() }
    override suspend fun getSettings() = saved.first().toEntity()

    override suspend fun getAppMode() = saved.first().appMode.toEntity()
    override suspend fun setAppMode(mode: Mode) {
        settings.updateData { it.toBuilder().setAppMode(mode.toProto()).build() }
    }

    override suspend fun getAppTheme(): Theme = saved.first().appTheme.toEntity()
    override suspend fun setAppTheme(theme: Theme) {
        settings.updateData { it.toBuilder().setAppTheme(theme.toProto()).build() }
    }

    override suspend fun getAppIcon(): Icon = saved.first().appIcon.toEntity()
    override suspend fun setAppIcon(icon: Icon) {
        settings.updateData { it.toBuilder().setAppIcon(icon.toProto()).build() }
    }

    override suspend fun getDefaultTab(): Tab = saved.first().defaultTab.toEntity()
    override suspend fun setDefaultTab(tab: Tab) {
        settings.updateData { it.toBuilder().setDefaultTab(tab.toProto()).build() }
    }

    override suspend fun checkFirstAppLaunch(): Boolean = saved.first().isFirstAppLaunch

    override suspend fun getDefaultRecipeLanguage(): Language = LanguageMapper.map(saved.first().defaultRecipeLanguage)
    override suspend fun setDefaultRecipeLanguage(language: Language) {
        settings.updateData { it.toBuilder().setDefaultRecipeLanguage(language.code).build() }
    }

    override suspend fun getOnlineRecipesLanguages(): List<String> = saved.first().onlineRecipesLanguagesList

    override suspend fun setOnlineRecipesLanguages(languages: List<String>) {
        val approvedLanguages = languages.filter { it.length == 2 }
        if (approvedLanguages.isEmpty()) throw IOException()
        settings.updateData { it.toBuilder().clearOnlineRecipesLanguages().addAllOnlineRecipesLanguages(approvedLanguages).build() }
    }

}
