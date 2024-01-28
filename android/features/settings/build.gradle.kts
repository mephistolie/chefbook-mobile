plugins {
  id("android-feature-module")
}

android {
  namespace = "io.chefbook.features.settings"

  buildFeatures {
    buildConfig = true
  }
}
composeDestinationsModuleName("settings")

dependencies {
  implementation(projects.common.sdk.settings.api.external)
}
