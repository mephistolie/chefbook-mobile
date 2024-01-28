import org.gradle.kotlin.dsl.getValue
import org.gradle.kotlin.dsl.getting

plugins {
  id("multiplatform-network-module")
  id("multiplatform-database-module")
}

kotlin {
  sourceSets {
    val commonMain by getting {
      dependencies {
        implementation(project(":common:sdk:core:api:internal"))
        implementation(project(":common:sdk:network:api:internal"))
        implementation(project(":common:sdk:database:api:internal"))
      }
    }
  }
}
