import org.gradle.accessors.dm.LibrariesForLibs
import org.gradle.kotlin.dsl.the

plugins {
  id("multiplatform-sdk-module")
  kotlin("plugin.serialization")
}

val libs = the<LibrariesForLibs>()

kotlin {
  sourceSets {
    val commonMain by getting {
      dependencies {
        implementation(libs.network.ktor.client.core)
        implementation(libs.network.ktor.client.serialization)
      }
    }
  }
}
