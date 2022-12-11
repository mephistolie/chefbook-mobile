package com.cactusknights

import android.app.Application
import com.cactusknights.chefbook.BuildConfig
import dagger.hilt.android.HiltAndroidApp
import java.security.Security
import javax.inject.Inject
import org.spongycastle.jce.provider.BouncyCastleProvider
import timber.log.Timber

@HiltAndroidApp
class ChefBookApp @Inject constructor(): Application() {

    override fun onCreate() {
        super.onCreate()
        Security.insertProviderAt(BouncyCastleProvider(), 1)

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}