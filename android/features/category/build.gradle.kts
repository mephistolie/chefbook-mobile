import utils.android.composeDestinationsModuleName

plugins {
  alias(libs.plugins.module.android.feature)
}

android.namespace = "io.chefbook.features.category"
composeDestinationsModuleName("category")

dependencies {
  implementation(projects.common.sdk.collection.api.external)

  implementation(libs.androidx.compose.uiTooling)
}
