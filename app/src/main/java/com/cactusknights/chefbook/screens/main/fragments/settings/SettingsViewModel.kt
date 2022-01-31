package com.cactusknights.chefbook.screens.main.fragments.settings

import android.content.ComponentName
import android.content.pm.PackageManager
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cactusknights.chefbook.SettingsProto
import com.cactusknights.chefbook.common.mvi.EventHandler
import com.cactusknights.chefbook.domain.usecases.SettingsUseCases
import com.cactusknights.chefbook.core.OldIconAlias
import com.cactusknights.chefbook.core.StandardIconAlias
import com.cactusknights.chefbook.screens.main.fragments.settings.models.SettingsScreenEvent
import com.cactusknights.chefbook.screens.main.fragments.settings.models.SettingsScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val settingsUseCases: SettingsUseCases
) : ViewModel(), EventHandler<SettingsScreenEvent> {

    private val _settingsState: MutableStateFlow<SettingsScreenState> = MutableStateFlow(SettingsScreenState.Loading)
    val settingsState: StateFlow<SettingsScreenState> = _settingsState.asStateFlow()

    init {
        viewModelScope.launch {
            settingsUseCases.listenToSettings().collect { _settingsState.emit(SettingsScreenState.SettingsLoaded(it)) }
        }
    }

    override fun obtainEvent(event: SettingsScreenEvent) {
        viewModelScope.launch {
            when (event) {
                is SettingsScreenEvent.SetDataSource -> { settingsUseCases.setDataSource(event.dataSource).collect{} }
                is SettingsScreenEvent.SetIcon -> { setIcon(event) }
                is SettingsScreenEvent.SetDefaultTab -> { settingsUseCases.setDefaultTab(event.tab).collect {} }
                is SettingsScreenEvent.SetTheme -> { settingsUseCases.setTheme(event.theme).collect {} }
            }
        }
    }

    private suspend fun setIcon(event: SettingsScreenEvent.SetIcon) {
        if (event.icon == SettingsProto.AppIcon.STANDARD) {
            event.activity.packageManager?.setComponentEnabledSetting(ComponentName(event.activity, StandardIconAlias::class.java), PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP)
            event.activity.packageManager?.setComponentEnabledSetting(ComponentName(event.activity, OldIconAlias::class.java), PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP)
        } else {
            event.activity.packageManager?.setComponentEnabledSetting(ComponentName(event.activity, StandardIconAlias::class.java), PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP)
            event.activity.packageManager?.setComponentEnabledSetting(ComponentName(event.activity, OldIconAlias::class.java), PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP)
        }
        settingsUseCases.setIcon(event.icon).collect{}
    }
}