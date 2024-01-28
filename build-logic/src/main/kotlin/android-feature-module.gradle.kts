import org.gradle.accessors.dm.LibrariesForLibs

plugins {
  id("android-compose-module")
  id("android-navigation-module")
}

val libs = the<LibrariesForLibs>()

dependencies {
  implementation(libs.di.koin.core)
  implementation(project(":android:core"))
  implementation(project(":android:design"))
  implementation(project(":android:ui"))
  implementation(project(":android:navigation"))

  implementation(project(":android:navigation"))
}
