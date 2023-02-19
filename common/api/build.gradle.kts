import com.mysty.chefbook.plugins.android.setNamespace

plugins {
  id("common-android-module")
  id("kotlin-kapt")
  kotlin("plugin.serialization") version "1.7.20"
  id("com.google.protobuf") version "0.9.1"
}

setNamespace("com.mysty.chefbook.api")

android {
  defaultConfig {
    minSdk = Config.minSdk
    consumerProguardFiles("proguard-rules.pro")
  }
}

dependencies {
  implementation(project(Modules.Common.core))

  implementation(Dependencies.Koin.core)
  implementation(Dependencies.Koin.androidCompat)

  implementation(Dependencies.serialization)

  implementation(Dependencies.SpongyCastle.core)
  implementation(Dependencies.SpongyCastle.prov)

  implementation(Dependencies.dataStore)
  implementation(Dependencies.protobuf)

  implementation(Dependencies.Room.runtime)
  implementation(Dependencies.Room.ktx)
  kapt(Dependencies.Room.compiler)

  implementation(Dependencies.Network.okHttp)
  implementation(Dependencies.Network.logging)
  implementation(Dependencies.Network.retrofit)
  implementation(Dependencies.Network.serializationConverter)

  implementation(Dependencies.Images.imageCompressor)

  coreLibraryDesugaring(Dependencies.desugar)
}

protobuf {
  protoc {
    artifact = "com.google.protobuf:protoc:3.21.11"
  }
  generateProtoTasks {
    all().forEach { task ->
      task.builtins {
        create("java") {
          option("lite")
        }
      }
    }
  }
}
