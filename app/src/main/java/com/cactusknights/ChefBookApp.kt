package com.cactusknights

import android.app.Application
import android.content.Intent
import androidx.appcompat.app.AppCompatDelegate
import androidx.datastore.core.DataStore
import com.cactusknights.chefbook.domain.ContentRepository
import com.cactusknights.chefbook.domain.SettingsRepository
import com.cactusknights.chefbook.models.AppTheme
import com.cactusknights.chefbook.models.SettingsScheme
import com.cactusknights.chefbook.repositories.SyncRepository
import com.cactusknights.chefbook.repositories.sync.SyncSettingsRepository
import com.cactusknights.chefbook.screens.auth.AuthActivity
import com.cactusknights.chefbook.screens.main.MainActivity
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import java.util.prefs.Preferences
import javax.inject.Inject

@HiltAndroidApp
class ChefBookApp: Application() {}