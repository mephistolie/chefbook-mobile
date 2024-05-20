import org.gradle.accessors.dm.LibrariesForLibs

plugins {
  id("multiplatform-sdk-module")
  kotlin("plugin.serialization")
}

val libs = the<LibrariesForLibs>()

kotlin {
  sourceSets {
    commonMain.dependencies {
      implementation(libs.network.ktor.client.core)
      implementation(libs.network.ktor.client.serialization)
    }
  }
}
