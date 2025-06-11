plugins {
  `kotlin-dsl`
  `kotlin-dsl-precompiled-script-plugins`
}

repositories {
  google()
  mavenCentral()
  gradlePluginPortal()
  maven(url = "https://jitpack.io")
}

dependencies {
  implementation(libs.gradlePlugin.kotlin)
  implementation(libs.kotlinx.serialization)
  implementation(libs.gradlePlugin.ksp)
  implementation(libs.gradlePlugin.swiftKLib)
  implementation(libs.gradlePlugin.composeMultiplatform)
  implementation(libs.gradlePlugin.composeCompiler)
  implementation(libs.gradlePlugin.android)

  compileOnly(files(libs.javaClass.superclass.protectionDomain.codeSource.location))
}
