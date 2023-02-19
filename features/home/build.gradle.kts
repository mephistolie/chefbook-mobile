
import com.mysty.chefbook.plugins.android.configureCompose
import com.mysty.chefbook.plugins.android.configureComposeDestinations
import com.mysty.chefbook.plugins.android.setFeatureNamespace

plugins {
  id("feature-module")
  id("ksp-module")
}

setFeatureNamespace("home")
configureCompose(
  extraDependencies = listOf(
    Dependencies.Compose.material,
    Dependencies.Compose.uiTooling,
    Dependencies.Images.coilCompose,
  )
)
configureComposeDestinations(moduleName = "home")
