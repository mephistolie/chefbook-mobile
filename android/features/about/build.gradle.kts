import utils.android.composeDestinationsModuleName

plugins {
  alias(libs.plugins.module.android.feature)
}

android {
  namespace = "io.chefbook.features.about"
  buildFeatures.buildConfig = true
}
composeDestinationsModuleName("about")

dependencies {
  implementation(libs.androidx.compose.uiTooling)
}
