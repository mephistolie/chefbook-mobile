package utils.android

import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.CommonExtension
import com.android.build.api.dsl.LibraryExtension
import org.gradle.accessors.dm.LibrariesForLibs
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.findByType
import org.jetbrains.kotlin.gradle.dsl.KotlinAndroidProjectExtension
import utils.gradle.BuildConfig
import utils.gradle.implementation
import utils.gradle.libs

internal fun Project.configureAndroidCommon() {
  apply(plugin = libs.plugins.kotlin.android.get().pluginId)

  kotlinExtension?.jvmToolchain(BuildConfig.JAVA_VERSION)
  androidExtension?.configureSdkVersions(libs)

  dependencies {
    implementation(libs.androidx.coreKtx)
    implementation(libs.androidx.coreKtx)
  }
}

internal fun Project.configureAndroidSdkVersions() {
  androidExtension?.configureSdkVersions(libs)
}


private fun CommonExtension<*, *, *, *, *, *>.configureSdkVersions(
  libs: LibrariesForLibs,
) {
  compileSdk = libs.versions.android.compileSdk.get().toInt()
  defaultConfig {
    minSdk = libs.versions.android.minSdk.get().toInt()
  }
}

internal val Project.androidExtension: CommonExtension<*, *, *, *, *, *>?
  get() = extensions.findByType(ApplicationExtension::class) ?: extensions.findByType(LibraryExtension::class)

internal val Project.kotlinExtension: KotlinAndroidProjectExtension?
  get() = extensions.findByType(KotlinAndroidProjectExtension::class)
