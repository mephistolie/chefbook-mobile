package com.cactusknights

import android.app.Application
import com.cactusknights.chefbook.BuildConfig
import com.cactusknights.chefbook.di.cacheModule
import com.cactusknights.chefbook.di.coroutinesModule
import com.cactusknights.chefbook.di.dataSourceModule
import com.cactusknights.chefbook.di.dataStoreModule
import com.cactusknights.chefbook.di.databaseModule
import com.cactusknights.chefbook.di.encryptionModule
import com.cactusknights.chefbook.di.networkModule
import com.cactusknights.chefbook.di.repositoryModule
import com.cactusknights.chefbook.di.useCasesModule
import com.cactusknights.chefbook.di.viewModelModule
import java.security.Security
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.spongycastle.jce.provider.BouncyCastleProvider
import timber.log.Timber

class ChefBookApp: Application() {

    override fun onCreate() {
        super.onCreate()
        configureBouncyCastle()
        configureTimber()
        configureKoin()
    }

    private fun configureBouncyCastle() {
        Security.insertProviderAt(BouncyCastleProvider(), 1)
    }

    private fun configureTimber() {
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }

    private fun configureKoin() {
        startKoin {
            androidContext(this@ChefBookApp)
            modules(
                coroutinesModule,
                cacheModule,
                dataSourceModule,
                dataStoreModule,
                encryptionModule,
                databaseModule,
                networkModule,
                repositoryModule,
                useCasesModule,
                viewModelModule,
            )
        }
    }
}
