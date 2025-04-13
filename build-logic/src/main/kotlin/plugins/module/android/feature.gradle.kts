package plugins.module.android

import utils.gradle.libs
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.project

plugins {
  id("plugins.module.android.compose")
  id("plugins.module.android.navigation")
}

dependencies {
  implementation(libs.di.koin.core)
  implementation(project(":android:core"))
  implementation(project(":android:design"))
  implementation(project(":android:ui"))
  implementation(project(":android:navigation"))

  implementation(project(":android:navigation"))
}
