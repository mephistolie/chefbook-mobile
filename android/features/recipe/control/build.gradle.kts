import utils.android.composeDestinationsModuleName

plugins {
  alias(libs.plugins.module.android.feature)
}

android.namespace = "io.chefbook.features.recipe.control"
composeDestinationsModuleName("recipe-control")

dependencies {
  implementation(projects.common.sdk.recipe.crud.api.external)
  implementation(projects.common.sdk.recipe.interaction.api.external)

  implementation(libs.coil.compose)
}
