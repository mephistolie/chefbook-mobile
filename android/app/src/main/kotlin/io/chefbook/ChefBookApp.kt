package io.chefbook

import android.app.Application
import com.google.crypto.tink.aead.AeadConfig
import com.google.crypto.tink.hybrid.HybridConfig
import io.chefbook.di.Modules
import io.chefbook.libs.logger.Logger
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class ChefBookApp : Application() {

  override fun onCreate() {
    super.onCreate()
    configureEncryption()
    configureLogger()
    configureDi()
  }

  private fun configureEncryption() {
    AeadConfig.register()
    HybridConfig.register()
  }

  private fun configureLogger() {
    if (BuildConfig.DEBUG) {
      Logger.plantDebug()
    }
  }

  private fun configureDi() {
    startKoin {
      androidContext(this@ChefBookApp)
      modules(Modules.all)
    }
  }
}
