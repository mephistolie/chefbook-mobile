package plugins.module.android

import utils.android.configureAndroidCommon
import utils.kotlin.enableExplicitBackingFields
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.project

plugins {
  id("com.android.library")
  id("kotlin-parcelize")
}

configureAndroidCommon()

enableExplicitBackingFields()

dependencies {
  implementation(project(":common:libs:logging"))
  implementation(project(":common:libs:coroutines"))
  implementation(project(":common:libs:utils"))
}
