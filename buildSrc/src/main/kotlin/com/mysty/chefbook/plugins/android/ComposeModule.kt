package com.mysty.chefbook.plugins.android

import Dependencies
import com.mysty.chefbook.plugins.android
import com.mysty.chefbook.plugins.ksp
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

fun Project.configureCompose(
  extraDependencies: List<String> = emptyList(),
) {
  android {
    buildFeatures.run {
      compose = true
    }

    composeOptions {
      kotlinCompilerExtensionVersion = Dependencies.Compose.version
    }
  }
  addBaseComposeDependencies(extraDependencies)
}

private fun Project.addBaseComposeDependencies(
  extraDependencies: List<String> = emptyList(),
) {
  dependencies {
    add("implementation", platform(Dependencies.Compose.bom))
    add("implementation", Dependencies.Compose.foundation)
    add("implementation", Dependencies.Compose.runtime)
    add("implementation", Dependencies.Compose.ui)
    extraDependencies.forEach { dependency -> add("implementation", dependency) }

    add("implementation", Dependencies.Compost.core)

    add("implementation", Dependencies.AndroidX.composeViewModel)

    // DI
    add("implementation", Dependencies.Koin.androidCompose)
  }
}

fun Project.configureComposeDestinations(moduleName: String? = null) {
  dependencies {
    add("implementation", Dependencies.Navigation.core)
    add("ksp", Dependencies.Navigation.ksp)
  }

  moduleName?.let {
    ksp {
      arg("compose-destinations.mode", "destinations")
      arg("compose-destinations.moduleName", moduleName)
    }
  }
}
