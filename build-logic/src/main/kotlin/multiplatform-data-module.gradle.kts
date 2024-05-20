plugins {
  id("multiplatform-network-module")
  id("multiplatform-database-module")
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
