import org.gradle.accessors.dm.LibrariesForLibs

plugins {
  id("multiplatform-sdk-module")
}

val libs = the<LibrariesForLibs>()

kotlin {
  sourceSets {
    val commonMain by getting {
      dependencies {
        implementation(libs.database.sqldelight.runtime)
      }
    }
  }
}
