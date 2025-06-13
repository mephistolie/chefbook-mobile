package io.chefbook

import android.app.Application
import io.chefbook.di.Modules
import io.chefbook.libs.logger.Logger
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class ChefBookApp : Application() {

  override fun onCreate() {
    super.onCreate()
    configureLogger()
    configureDi()
  }

  private fun configureLogger() {
    if (BuildConfig.DEBUG) {
      Logger.initDebug()
    }
  }

  private fun configureDi() {
    startKoin {
      androidContext(this@ChefBookApp)
      modules(Modules.all())
    }
  }
}
