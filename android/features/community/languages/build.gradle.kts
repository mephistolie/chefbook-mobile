plugins {
  id("android-feature-module")
}

android.namespace = "io.chefbook.features.community.recipes.dashboard"
composeDestinationsModuleName("community-languages")

dependencies {
  implementation(projects.common.sdk.settings.api.external)
}
