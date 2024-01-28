plugins {
  id("android-feature-module")
}

android {
  namespace = "io.chefbook.features.about"

  buildFeatures {
    buildConfig = true
  }
}
composeDestinationsModuleName("about")

dependencies {
  implementation(libs.androidx.compose.uiTooling)
}
