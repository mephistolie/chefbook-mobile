plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
    kotlin("plugin.serialization") version "1.7.20"
    id("com.google.protobuf") version "0.9.1"
}

android {
    namespace = "com.mysty.chefbook.api"
    compileSdk = Project.compileSdk

    defaultConfig {
        minSdk = Project.minSdk
        consumerProguardFiles("proguard-rules.pro")
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
        isCoreLibraryDesugaringEnabled = true
    }
    kotlinOptions {
        jvmTarget = "${JavaVersion.VERSION_11}"
    }
}

dependencies {
    implementation(project(Modules.Common.core))

    implementation(Dependencies.timber)

    implementation(Dependencies.Koin.core)

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
