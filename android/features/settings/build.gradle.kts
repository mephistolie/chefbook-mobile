import utils.android.composeDestinationsModuleName

plugins {
  alias(libs.plugins.module.android.feature)
}

android {
  namespace = "io.chefbook.features.settings"
  buildFeatures.buildConfig = true
}
composeDestinationsModuleName("settings")

dependencies {
  implementation(projects.common.sdk.settings.api.external)
}
