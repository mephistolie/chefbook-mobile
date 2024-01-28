import org.gradle.accessors.dm.LibrariesForLibs

plugins {
  id("android-ksp-module")
}

val libs = the<LibrariesForLibs>()

ksp {
  arg("compose-destinations.mode", "destinations")
}

dependencies {
  implementation(libs.composeDestinations.core)
  ksp(libs.composeDestinations.ksp)
}
