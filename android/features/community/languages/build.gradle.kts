import utils.android.composeDestinationsModuleName

plugins {
  alias(libs.plugins.module.android.feature)
}

android.namespace = "io.chefbook.features.community.recipes.dashboard"
composeDestinationsModuleName("community-languages")

dependencies {
  implementation(projects.common.sdk.settings.api.external)
}
