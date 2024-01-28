import org.jetbrains.kotlin.gradle.dsl.kotlinExtension

plugins {
  id("android-base-module")
  id("com.google.devtools.ksp")
}

kotlinExtension.sourceSets.forEach { sourceSet ->
  when (sourceSet.name) {
    "debug" -> sourceSet.kotlin.srcDir("build/generated/ksp/debug/kotlin")
    "release" -> sourceSet.kotlin.srcDir("build/generated/ksp/release/kotlin")
  }
}
