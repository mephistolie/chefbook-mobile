import utils.android.composeDestinationsModuleName

plugins {
  alias(libs.plugins.module.android.feature)
}

android.namespace = "io.chefbook.features.encryption"
composeDestinationsModuleName("encryption")

dependencies {
  implementation(projects.common.sdk.encryption.vault.api.external)

  implementation(libs.androidx.compose.uiTooling)
}
