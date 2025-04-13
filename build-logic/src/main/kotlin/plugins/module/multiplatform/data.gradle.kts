package plugins.module.multiplatform

plugins {
  id("plugins.module.multiplatform.database")
  id("plugins.module.multiplatform.network")
}

kotlin {
  sourceSets {
    commonMain.dependencies {
      implementation(project(":common:sdk:core:api:internal"))
      implementation(project(":common:sdk:network:api:internal"))
      implementation(project(":common:sdk:database:api:internal"))
    }
  }
}
