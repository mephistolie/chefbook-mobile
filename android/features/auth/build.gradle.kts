plugins {
  id("android-feature-module")
}

android {
  namespace = "io.chefbook.features.auth.form"

  buildTypes {
    getByName("release") {
      isMinifyEnabled = true
      proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
    }
  }
}
composeDestinationsModuleName("auth-form")

dependencies {
  implementation(projects.common.libs.exceptions)
  implementation(projects.common.libs.utils)
  implementation(projects.common.sdk.auth.api.external)

  implementation(libs.androidx.compose.uiTooling)
  implementation(libs.androidx.credentials)
  implementation(libs.credentials.google)
}
