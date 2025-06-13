plugins {
  alias(libs.plugins.module.multiplatform.sdk)
}

kotlin {
  sourceSets {
    commonMain.dependencies {
      api(projects.common.sdk.collection.api.external)
      api(projects.common.sdk.tag.api.external)
    }
  }
}
