package utils.android

import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.CommonExtension
import com.android.build.api.dsl.KotlinMultiplatformAndroidLibraryExtension
import com.android.build.api.dsl.LibraryExtension
import org.gradle.accessors.dm.LibrariesForLibs
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.findByType
import org.jetbrains.kotlin.gradle.dsl.KotlinAndroidProjectExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import utils.gradle.BuildConfig
import utils.gradle.implementation
import utils.gradle.libs

internal fun Project.configureAndroidCommon() {
  apply(plugin = libs.plugins.kotlin.android.get().pluginId)

  kotlinExtension?.jvmToolchain(BuildConfig.JAVA_VERSION)
  nativeAndroidExtension?.configureSdkVersions(libs)

  dependencies {
    implementation(libs.androidx.coreKtx)
    implementation(libs.androidx.coreKtx)
  }
}

internal fun Project.configureAndroidSdkVersions() {
  nativeAndroidExtension?.configureSdkVersions(libs)
}

internal fun KotlinMultiplatformExtension.kmpAndroidTarget() {
  multiplatformAndroidExtension?.apply {
    namespace = project.defaultKmpNamespace
    compileSdk = project.libs.versions.android.compileSdk.get().toInt()
    minSdk = project.libs.versions.android.minSdk.get().toInt()
  }
}

private fun CommonExtension<*, *, *, *, *, *>.configureSdkVersions(
  libs: LibrariesForLibs,
) {
  compileSdk = libs.versions.android.compileSdk.get().toInt()
  defaultConfig {
    minSdk = libs.versions.android.minSdk.get().toInt()
  }
}

internal val Project.nativeAndroidExtension: CommonExtension<*, *, *, *, *, *>?
  get() = extensions.findByType(ApplicationExtension::class)
    ?: extensions.findByType(LibraryExtension::class)

internal val KotlinMultiplatformExtension.multiplatformAndroidExtension: KotlinMultiplatformAndroidLibraryExtension?
  get() = extensions.findByType(KotlinMultiplatformAndroidLibraryExtension::class)

internal val Project.kotlinExtension: KotlinAndroidProjectExtension?
  get() = extensions.findByType(KotlinAndroidProjectExtension::class)

internal val Project.defaultKmpNamespace: String
  get() = "io.chefbook.${project.path.drop(":common:".length).replace(":", ".")}"
