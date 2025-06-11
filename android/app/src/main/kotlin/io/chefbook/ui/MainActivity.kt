package io.chefbook.ui

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import com.arkivanov.decompose.defaultComponentContext
import io.chefbook.features.root.RootComponentImpl
import io.chefbook.ui.delegates.IconSwitcherDelegate
import io.chefbook.ui.screens.main.RootScreen
import org.koin.android.ext.android.inject

class MainActivity : AppCompatActivity() {

  private val iconSwitcherDelegate: IconSwitcherDelegate by inject()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    val rootComponent = RootComponentImpl(componentContext = defaultComponentContext())

    setContent { RootScreen(component = rootComponent) }
  }

  override fun onPause() {
    super.onPause()
    iconSwitcherDelegate.disableUnselectedIcons()
  }
}
