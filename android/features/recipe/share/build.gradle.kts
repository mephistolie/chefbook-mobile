import utils.android.composeDestinationsModuleName

plugins {
  alias(libs.plugins.module.android.feature)
}

android.namespace = "io.chefbook.features.recipe.share"
composeDestinationsModuleName("recipe-share")

dependencies {
  implementation(projects.common.sdk.recipe.crud.api.external)

  implementation(libs.coil.compose)
}
