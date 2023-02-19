plugins {
  `java-gradle-plugin`
  `kotlin-dsl`
  kotlin("jvm") version "1.8.10" apply false
}

gradlePlugin {
  plugins {
    register("common-java-module") {
      id = "common-java-module"
      implementationClass = "com.mysty.chefbook.plugins.java.CommonJavaModule"
    }
    register("common-android-module") {
      id = "common-android-module"
      implementationClass = "com.mysty.chefbook.plugins.android.CommonAndroidModule"
    }
    register("app-module") {
      id = "app-module"
      implementationClass = "com.mysty.chefbook.plugins.android.AppModule"
    }
    register("feature-module") {
      id = "feature-module"
      implementationClass = "com.mysty.chefbook.plugins.android.FeatureModule"
    }
    register("ksp-module") {
      id = "ksp-module"
      implementationClass = "com.mysty.chefbook.plugins.android.KspModule"
    }
  }
}

repositories {
  google()
  mavenCentral()
}

dependencies {
  compileOnly(gradleApi())

  implementation("com.android.tools.build:gradle:7.4.0-rc01")
  implementation(kotlin("gradle-plugin", "1.8.10"))
  implementation(kotlin("android-extensions"))
  implementation("com.google.devtools.ksp:symbol-processing-gradle-plugin:1.8.10-1.0.9")

}
