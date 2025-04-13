import utils.gradle.libs

plugins {
  alias(libs.plugins.module.android.lib)
}

android.namespace = "io.chefbook.libs.mvi"

dependencies {
  implementation(libs.androidx.lifecycle.viewmodel.compose)
}
