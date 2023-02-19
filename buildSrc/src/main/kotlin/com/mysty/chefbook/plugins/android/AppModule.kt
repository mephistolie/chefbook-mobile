package com.mysty.chefbook.plugins.android

import com.mysty.chefbook.plugins.android
import com.mysty.chefbook.plugins.kapt
import com.mysty.chefbook.plugins.plugins
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies


open class AppModule : AndroidModule() {

  override fun apply(project: Project) {
    project.run {
      plugins {
        apply("com.android.application")
        apply("kotlin-android")
        apply("kotlin-kapt")
        apply("com.google.gms.google-services")
        apply("com.google.firebase.crashlytics")
      }
      super.apply(this)
      android {
        defaultConfig {
            applicationId = Config.applicationId
            targetSdk = Config.targetSdk
            versionCode = Config.version
            versionName = Config.versionName
            multiDexEnabled = true

            testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        }
        buildTypes {
          getByName(BuildTypes.release) {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
              getDefaultProguardFile("proguard-android-optimize.txt"),
              "proguard-rules.pro"
            )
          }
        }
      }
      kapt {
        correctErrorTypes = true
      }
      includeCommonModules()
      includeFeaturesModules()
    }
  }

}

fun Project.includeFeaturesModules() {
  dependencies {
    add("implementation", project(Modules.Featues.home))
    add("implementation", project(Modules.Featues.auth))
    add("implementation", project(Modules.Featues.about))
    add("implementation", project(Modules.Featues.encryption))
    add("implementation", project(Modules.Featues.category))

    add("implementation", project(Modules.Featues.Recipe.info))
    add("implementation", project(Modules.Featues.Recipe.share))
    add("implementation", project(Modules.Featues.Recipe.control))
    add("implementation", project(Modules.Featues.Recipe.input))

    add("implementation", project(Modules.Featues.RecipeBook.dashboard))
    add("implementation", project(Modules.Featues.RecipeBook.search))
    add("implementation", project(Modules.Featues.RecipeBook.favourite))
    add("implementation", project(Modules.Featues.RecipeBook.category))

    add("implementation", project(Modules.Featues.ShoppingList.control))
    add("implementation", project(Modules.Featues.ShoppingList.purchaseInput))
  }
}
