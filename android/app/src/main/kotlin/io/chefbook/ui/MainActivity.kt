package io.chefbook.ui

import android.os.Bundle
import android.view.SurfaceView
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import com.arkivanov.decompose.defaultComponentContext
import io.chefbook.features.root.component.RootComponentImpl
import io.chefbook.ui.delegates.IconSwitcherDelegate
import io.chefbook.features.root.ui.RootScreen
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.sync.Mutex
import org.koin.android.ext.android.inject
import kotlin.coroutines.suspendCoroutine

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
