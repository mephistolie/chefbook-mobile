object Dependencies {

    const val gradle = "com.android.tools.build:gradle:${Project.gradleVersion}"
    const val kotlinPlugin = "org.jetbrains.kotlin:kotlin-gradle-plugin:${Project.kotlinVersion}"
    const val gmsPlugin = "com.google.gms:google-services:4.3.14"

    object AndroidX {
        const val core = "androidx.core:core-ktx:1.9.0"
        const val appCompat = "androidx.appcompat:appcompat:1.5.1"
        const val preference = "androidx.preference:preference-ktx:1.2.0"

        const val composeActivity = "androidx.activity:activity-compose:1.6.1"
        const val composeViewModel = "androidx.lifecycle:lifecycle-viewmodel-compose:2.5.1"
        const val composeHilt = "androidx.hilt:hilt-navigation-compose:1.0.0"
        const val composeNavigation = "androidx.navigation:navigation-compose:2.5.3"
    }

    object Compose {
        const val version = "1.3.2"

        const val compiler = "androidx.compose.compiler:compiler:$version"
        const val foundation = "androidx.compose.foundation:foundation:1.3.1"
        const val runtime = "androidx.compose.runtime:runtime:$version"
        const val ui = "androidx.compose.ui:ui:$version"
        const val uiTooling = "androidx.compose.ui:ui-tooling:$version"
        const val material = "androidx.compose.material:material:1.3.1"
        const val animation = "androidx.compose.animation:animation:$version"
    }

    object Accompanist {
        private const val version = "0.28.0"

        const val systemUiController = "com.google.accompanist:accompanist-systemuicontroller:$version"
        const val flowLayout = "com.google.accompanist:accompanist-flowlayout:$version"
        const val navigationMaterial = "com.google.accompanist:accompanist-navigation-material:$version"
        const val navigationAnimation = "com.google.accompanist:accompanist-navigation-animation:$version"
        const val pager = "com.google.accompanist:accompanist-pager:$version"
        const val pagerIndicator = "com.google.accompanist:accompanist-pager-indicators:$version"
    }

    object Compost {
        private const val version = "0.0.4"

        const val core = "com.github.mephistolie.compost:core:$version"
        const val ui = "com.github.mephistolie.compost:ui:$version"
    }

    const val composeReorderable = "org.burnoutcrew.composereorderable:reorderable:0.9.2"

    const val timber = "com.jakewharton.timber:timber:5.0.1"

    object Hilt {
        private const val version = "2.42"

        const val plugin = "com.google.dagger:hilt-android-gradle-plugin:$version"
        const val library = "com.google.dagger:hilt-android:$version"
        const val compiler = "com.google.dagger:hilt-compiler:$version"
    }

    const val serialization = "org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.3"

    const val dataStore = "androidx.datastore:datastore:1.0.0"
    const val protobuf = "com.google.protobuf:protobuf-javalite:3.17.2"

    object Room {
        private const val version = "2.4.2"

        const val runtime = "androidx.room:room-runtime:$version"
        const val ktx = "androidx.room:room-ktx:$version"
        const val compiler = "androidx.room:room-compiler:$version"
    }

    object Network {
        private const val okHttpVersion = "4.9.0"
        private const val retrofitVersion = "2.9.0"

        const val okHttp = "com.squareup.okhttp3:okhttp:$okHttpVersion"
        const val logging = "com.squareup.okhttp3:logging-interceptor:$okHttpVersion"
        const val retrofit = "com.squareup.retrofit2:retrofit:$retrofitVersion"
        const val kotlinSerialization = "com.jakewharton.retrofit:retrofit2-kotlinx-serialization-converter:0.8.0"
    }

    object Images {
        const val coil = "io.coil-kt:coil:2.2.2"
        const val coilCompose = "io.coil-kt:coil-compose:2.1.0"

        const val imageCropper = "com.github.CanHub:Android-Image-Cropper:4.0.0"

        const val imageCompressor = "id.zelory:compressor:3.0.1"

        const val zxing = "com.google.zxing:core:3.4.1"
    }

    object Firebase {
        const val crashlytics = "com.google.firebase:firebase-crashlytics-ktx:18.3.2"
        const val crashlyticsPlugin = "com.google.firebase:firebase-crashlytics-gradle:2.9.2"
    }

    object Tests {
        const val jUnit = "junit:junit:4.13.2"
        const val jUnitAndroid = "androidx.test.ext:junit:1.1.4"
        const val espresso = "androidx.test.espresso:espresso-core:3.5.0"
    }

    const val desugar = "com.android.tools:desugar_jdk_libs:2.0.0"
}
