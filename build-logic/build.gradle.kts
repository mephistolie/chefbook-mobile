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
  implementation(libs.kotlin.gradlePlugin)
  implementation(libs.kotlin.serialization)
  implementation(libs.android.gradlePlugin)
  implementation(libs.ksp.gradlePlugin)

  compileOnly(files(libs.javaClass.superclass.protectionDomain.codeSource.location))
}

versionCatalogs {
  this.named("libs")
}
