import utils.kotlin.OptIns

plugins {
  alias(libs.plugins.module.multiplatform.base)
}

kotlin {
  sourceSets {
    commonMain.dependencies {
      implementation(projects.common.libs.logging)
    }

    compilerOptions {
      optIn.addAll(
        OptIns.ExperimentalForeignApi,
        OptIns.BetaInteropApi,
      )
    }
  }
}
