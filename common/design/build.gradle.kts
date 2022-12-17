plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.mysty.chefbook.design"
    compileSdk = Project.compileSdk

    defaultConfig {
        minSdk = Project.minSdk
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    composeOptions {
        kotlinCompilerExtensionVersion = Dependencies.Compose.version
    }

    kotlinOptions {
        jvmTarget = "${JavaVersion.VERSION_11}"
    }

    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation(project(Modules.Common.core))
    implementation(project(Modules.Common.coreUi))

    implementation(Dependencies.Compose.runtime)
    implementation(Dependencies.Compose.ui)
    implementation(Dependencies.Compose.uiTooling)

    implementation(Dependencies.Accompanist.pager)
    implementation(Dependencies.Accompanist.pagerIndicator)

    implementation(Dependencies.Compost.core)
    implementation(Dependencies.Compost.ui)

    implementation(Dependencies.Images.coil)
    implementation(Dependencies.Images.coilCompose)
}
