
import com.mysty.chefbook.plugins.android.configureCompose
import com.mysty.chefbook.plugins.android.configureComposeDestinations
import com.mysty.chefbook.plugins.android.setFeatureNamespace

plugins {
  id("feature-module")
  id("ksp-module")
}

setFeatureNamespace("auth")
configureCompose(
  extraDependencies = listOf(
    Dependencies.Compose.material,
    Dependencies.Compose.uiTooling
  )
)
configureComposeDestinations(moduleName = "auth")
