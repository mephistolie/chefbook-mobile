package io.chefbook.features.about.ui

import android.os.Build
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.ramcosta.composedestinations.annotation.Destination
import io.chefbook.features.about.BuildConfig
import io.chefbook.features.about.ui.mvi.AboutScreenEffect
import io.chefbook.navigation.navigators.BaseNavigator
import org.koin.androidx.compose.koinViewModel

@Destination(route = "about")
@Composable
internal fun AboutScreen(
  navigator: BaseNavigator,
) {
  val viewModel = koinViewModel<AboutScreenViewModel>()

  val context =  LocalContext.current
  val packageManager = remember { context.packageManager.getPackageInfo(context.packageName, 0) }

  AboutScreenContent(
    versionName = packageManager.versionName,
    versionCode = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
      packageManager.longVersionCode
    } else {
      packageManager.versionCode.toLong()
    },
    buildType = BuildConfig.BUILD_TYPE,
    onIntent = { event -> viewModel.handleIntent(event) },
  )

  LaunchedEffect(Unit) {
    viewModel.effect.collect { effect ->
      when (effect) {
        is AboutScreenEffect.OnBack -> navigator.navigateUp()
      }
    }
  }
}
