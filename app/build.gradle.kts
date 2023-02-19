import com.mysty.chefbook.plugins.android.configureCompose
import com.mysty.chefbook.plugins.android.configureComposeDestinations

plugins {
  id("app-module")
  id("ksp-module")
}

configureCompose(
  extraDependencies = listOf(
    Dependencies.Compose.compiler,
    Dependencies.Compose.animation,
    Dependencies.Compose.material,
    Dependencies.Compose.uiTooling,
    Dependencies.Accompanist.systemUiController,
    Dependencies.Accompanist.navigationMaterial,
    Dependencies.Accompanist.navigationAnimation,
    Dependencies.Images.coilCompose,
    Dependencies.AndroidX.composeActivity,
  )
)
configureComposeDestinations()

dependencies {

  // DI
  implementation(Dependencies.Koin.core)
  implementation(Dependencies.Koin.android)

  // Encryption
  implementation(Dependencies.SpongyCastle.prov)

  // Crashlytics
  implementation(Dependencies.Firebase.crashlytics)

}
