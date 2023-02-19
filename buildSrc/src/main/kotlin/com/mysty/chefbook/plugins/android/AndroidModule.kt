package com.mysty.chefbook.plugins.android

import Config
import com.mysty.chefbook.plugins.android
import com.mysty.chefbook.plugins.plugins
import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

open class AndroidModule : Plugin<Project> {

  override fun apply(project: Project) = project.configureAndroidModule()

  private fun Project.configureAndroidModule() {
    plugins {
      apply("kotlin-parcelize")
    }

    android {
      compileSdkVersion(Config.compileSdk)
      defaultConfig {
        minSdk = Config.minSdk
      }

      compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
      }

      tasks.withType(KotlinCompile::class.java).configureEach {
        kotlinOptions {
          jvmTarget = "${JavaVersion.VERSION_11}"
        }
      }
    }
    addBaseDependencies()
  }

  private fun Project.addBaseDependencies() {
    android {
      dependencies {
        add("implementation", Dependencies.AndroidX.appCompat)
        add("implementation", Dependencies.AndroidX.core)

        add("implementation", Dependencies.timber)
      }
    }
  }

}

fun Project.setNamespace(moduleNamespace: String) {
  android {
    namespace = moduleNamespace
  }
}