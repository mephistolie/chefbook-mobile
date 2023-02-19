buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath(Dependencies.androidGradlePlugin)
        classpath(Dependencies.kotlinPlugin)

        classpath(Dependencies.gmsPlugin)
        classpath(Dependencies.Firebase.crashlyticsPlugin)

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
