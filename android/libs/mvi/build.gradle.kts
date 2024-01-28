plugins {
  id("android-base-module")
}

android.namespace = "io.chefbook.libs.mvi"

dependencies {
  implementation(libs.androidx.lifecycle.viewmodel.compose)
}
