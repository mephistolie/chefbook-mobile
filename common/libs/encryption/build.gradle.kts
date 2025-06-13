import utils.kotlin.OptIns

plugins {
  alias(libs.plugins.module.multiplatform.base)
  alias(libs.plugins.swiftKLib)
}

kotlin {
  sourceSets {
    commonMain.dependencies {
      implementation(projects.common.libs.utils)
    }
    iosMain.dependencies {
      implementation(libs.cryptographyKotlin.asn1)
      implementation(libs.bundles.hash)
    }
    val iosTargets = listOf(iosX64(), iosArm64(), iosSimulatorArm64())
    iosTargets.forEach { target ->
      target.compilations {
        val main by getting {
          cinterops {
            create("swift")
          }
        }
      }
    }

    commonTest.dependencies {
      implementation(libs.kotlin.test)
      implementation(libs.network.ktor.client.core)
    }

    compilerOptions {
      optIn.addAll(
        OptIns.ExperimentalForeignApi,
        OptIns.BetaInteropApi,
      )
    }
  }

  androidLibrary {
    withHostTestBuilder {}
  }
}

swiftklib {
  create("swift") {
    path = file("src/iosMain/swift")
    packageName("io.chefbook.libs.crypto.encryption")
  }
}
