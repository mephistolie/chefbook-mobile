plugins {
  id("android-feature-module")
}

android.namespace = "io.chefbook.features.community.recipes"
composeDestinationsModuleName("community-recipes")

dependencies {
  implementation(projects.common.sdk.settings.api.external)
  implementation(projects.common.sdk.profile.api.external)
  implementation(projects.common.sdk.recipe.community.api.external)
  implementation(projects.common.sdk.tag.api.external)

  implementation(libs.coil.compose)
}
