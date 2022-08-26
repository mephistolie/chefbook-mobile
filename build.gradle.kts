// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {

        val hiltVersion = "2.42"
        val kotlinVersion = "1.7.0"

        classpath("com.android.tools.build:gradle:7.2.2")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion")

        classpath("com.google.gms:google-services:4.3.13")
        classpath("com.google.firebase:firebase-crashlytics-gradle:2.9.1")

        classpath("com.google.dagger:hilt-android-gradle-plugin:$hiltVersion")

        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle files
    }
}

allprojects {
    repositories {
        google()
        mavenCentral()
        maven(url = "https://jitpack.io")
    }
}

tasks.create<Delete>("clean") {
    delete {
        rootProject.buildDir
    }
}