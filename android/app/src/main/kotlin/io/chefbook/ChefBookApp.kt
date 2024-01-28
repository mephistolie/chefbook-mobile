package io.chefbook

import android.app.Application
import io.chefbook.di.Modules
import io.chefbook.libs.logger.Logger
import java.security.Security
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.spongycastle.jce.provider.BouncyCastleProvider

class ChefBookApp : Application() {

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
      Logger.plantDebug()
  }

  private fun configureKoin() {
    startKoin {
      androidContext(this@ChefBookApp)
      modules(Modules.all)
    }
  }
}
