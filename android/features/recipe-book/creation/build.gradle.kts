import utils.android.composeDestinationsModuleName

plugins {
  alias(libs.plugins.module.android.feature)
}

android.namespace = "io.chefbook.features.recipebook.creation"
composeDestinationsModuleName("recipebook-creation")
