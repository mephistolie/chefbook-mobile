package com.cactusknights.chefbook.screens.main.fragments.settings

import android.content.ComponentName
import android.content.pm.PackageManager
import androidx.core.content.ContentProviderCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cactusknights.chefbook.base.EventHandler
import com.cactusknights.chefbook.common.Result
import com.cactusknights.chefbook.domain.usecases.SettingsUseCases
import com.cactusknights.chefbook.models.AppIcon
import com.cactusknights.chefbook.screens.main.fragments.settings.aliases.OldIconAlias
import com.cactusknights.chefbook.screens.main.fragments.settings.aliases.StandardIconAlias
import com.cactusknights.chefbook.screens.main.fragments.settings.models.SettingsEvent
import com.cactusknights.chefbook.screens.main.fragments.settings.models.SettingsState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val settingsUseCases: SettingsUseCases
) : ViewModel(), EventHandler<SettingsEvent> {

    private val _settingsState: MutableStateFlow<SettingsState> = MutableStateFlow(SettingsState(settingsUseCases.getSettings()))
    val settingsState: StateFlow<SettingsState> = _settingsState.asStateFlow()

    override fun obtainEvent(event: SettingsEvent) {
        viewModelScope.launch {
            when (event) {
                is SettingsEvent.SetDataSource -> {
                    settingsUseCases.setDataSource(event.dataSource).collect{ result ->
                        if (result is Result.Success) _settingsState.emit(SettingsState(settingsState.value.settings.copy(dataSource = event.dataSource)))
                    }
                }
                is SettingsEvent.SetIcon -> {
                    if (event.icon == AppIcon.STANDARD) {
                        event.activity.packageManager?.setComponentEnabledSetting(ComponentName(event.activity, StandardIconAlias::class.java), PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP)
                        event.activity.packageManager?.setComponentEnabledSetting(ComponentName(event.activity, OldIconAlias::class.java), PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP)
                    } else {
                        event.activity.packageManager?.setComponentEnabledSetting(ComponentName(event.activity, StandardIconAlias::class.java), PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP)
                        event.activity.packageManager?.setComponentEnabledSetting(ComponentName(event.activity, OldIconAlias::class.java), PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP)
                    }
                    settingsUseCases.setIcon(event.icon).collect{ result ->
                        if (result is Result.Success) _settingsState.emit(SettingsState(settingsState.value.settings.copy(icon = event.icon)))
                    }
                }
                is SettingsEvent.SetShoppingListDefault -> {
                    settingsUseCases.setShoppingListDefault(event.isDefault).collect { result ->
                        if (result is Result.Success) _settingsState.emit(SettingsState(settingsState.value.settings.copy(isShoppingListDefault = event.isDefault)))
                    }

                }
                is SettingsEvent.SetTheme -> {
                    settingsUseCases.setTheme(event.theme).collect { result ->
                        if (result is Result.Success) _settingsState.emit(SettingsState(settingsState.value.settings.copy(theme = event.theme)))
                    }
                }
            }
        }
    }
}