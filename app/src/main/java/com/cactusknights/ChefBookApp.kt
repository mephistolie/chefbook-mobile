package com.cactusknights

import android.app.Application
import com.cactusknights.chefbook.BuildConfig
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject
import timber.log.Timber

@HiltAndroidApp
class ChefBookApp @Inject constructor(): Application() {

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}