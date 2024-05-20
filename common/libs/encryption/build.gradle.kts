plugins {
  id("multiplatform-base-module")
  kotlin("native.cocoapods")
}

kotlin {
  sourceSets {
    androidMain.dependencies {
      implementation(libs.tinkAndroid)
    }

    commonTest.dependencies {
      implementation(libs.kotlin.test)
    }
    androidUnitTest.dependencies {
      implementation(libs.kotlin.test.junit)
    }
  }

  cocoapods {
    version = "1.0"
    ios.deploymentTarget = "15.0"
  }
}

android.namespace = "io.chefbook.libs.encryption"
