plugins {
  alias(libs.plugins.module.android.feature)
}

android.namespace = "io.chefbook.features.recipebook.core"

dependencies {
  implementation(projects.common.sdk.recipe.core.api.external)

  implementation(libs.coil.compose)
}
