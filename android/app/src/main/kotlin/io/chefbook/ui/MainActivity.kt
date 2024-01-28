package io.chefbook.ui

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import io.chefbook.ui.delegates.IconSwitcherDelegate
import io.chefbook.ui.screens.main.AppScreen
import org.koin.android.ext.android.inject

class MainActivity : AppCompatActivity() {

  private val iconSwitcherDelegate: IconSwitcherDelegate by inject()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    WindowCompat.setDecorFitsSystemWindows(window, false)

    setContent { AppScreen() }
  }

  override fun onPause() {
    super.onPause()
    iconSwitcherDelegate.disableUnselectedIcons()
  }
}
