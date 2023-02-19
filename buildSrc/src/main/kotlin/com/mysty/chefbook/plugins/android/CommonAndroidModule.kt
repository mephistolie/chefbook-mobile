package com.mysty.chefbook.plugins.android

import com.mysty.chefbook.plugins.android
import com.mysty.chefbook.plugins.plugins
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies


open class CommonAndroidModule : AndroidModule() {

  override fun apply(project: Project) {
    project.run {
      plugins {
        apply("com.android.library")
        apply("org.jetbrains.kotlin.android")
      }
      super.apply(this)
    }
  }

}

