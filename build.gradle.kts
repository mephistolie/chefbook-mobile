// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath(Dependencies.gradle)
        classpath(Dependencies.kotlinPlugin)

        classpath(Dependencies.gmsPlugin)
        classpath(Dependencies.Firebase.crashlyticsPlugin)

        classpath(Dependencies.Hilt.plugin)
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.7.20")
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