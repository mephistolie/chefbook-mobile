package plugins.module.android

import org.gradle.kotlin.dsl.dependencies
import utils.gradle.libs
import utils.kotlin.configureKsp

plugins {
  id("plugins.module.android.lib")
  id("com.google.devtools.ksp")
}

configureKsp()

ksp {
  arg("compose-destinations.mode", "destinations")
}

dependencies {
  implementation(libs.composeDestinations.core)

  ksp(libs.composeDestinations.ksp)
}
