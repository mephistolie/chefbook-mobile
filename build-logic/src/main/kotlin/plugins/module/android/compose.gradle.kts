package plugins.module.android

import utils.android.configureCompose
import utils.gradle.libs
import org.gradle.kotlin.dsl.dependencies

plugins {
  id("plugins.module.android.lib")
}

configureCompose()

dependencies {
  implementation(libs.androidx.lifecycle.compose)
  implementation(libs.androidx.lifecycle.viewmodel.compose)
  implementation(libs.di.koin.compose)

  implementation(libs.compost.core)
}
