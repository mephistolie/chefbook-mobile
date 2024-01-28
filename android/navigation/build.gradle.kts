plugins {
  id("android-compose-module")
  id("android-navigation-module")
}

android.namespace = "io.chefbook.navigation"

dependencies {
  implementation(projects.android.core)
  implementation(projects.android.design)
}
