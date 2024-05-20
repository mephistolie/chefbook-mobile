package io.chefbook.ui

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import coil.Coil
import coil.ImageLoader
import io.chefbook.libs.logger.Logger
import io.chefbook.ui.delegates.IconSwitcherDelegate
import io.chefbook.ui.screens.main.AppScreen
import okhttp3.OkHttpClient
import org.koin.android.ext.android.get
import org.koin.android.ext.android.inject
import org.koin.compose.koinInject

class MainActivity : AppCompatActivity() {

  private val iconSwitcherDelegate: IconSwitcherDelegate by inject()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()

    setContent { AppScreen() }
  }

  override fun onPause() {
    super.onPause()
    iconSwitcherDelegate.disableUnselectedIcons()
  }
}
