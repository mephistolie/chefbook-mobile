import org.gradle.accessors.dm.LibrariesForLibs
import org.gradle.kotlin.dsl.getValue
import org.gradle.kotlin.dsl.getting

plugins {
  id("multiplatform-base-module")
}

val libs = the<LibrariesForLibs>()

kotlin {
  sourceSets {
    val commonMain by getting {
      dependencies {
        implementation(project(":common:libs:models"))
        implementation(project(":common:libs:utils"))
        implementation(project(":common:libs:di"))
        implementation(project(":common:libs:coroutines"))
        implementation(project(":common:libs:exceptions"))
        implementation(project(":common:libs:logger"))

        implementation(libs.di.koin.core)
      }
    }
  }
}
