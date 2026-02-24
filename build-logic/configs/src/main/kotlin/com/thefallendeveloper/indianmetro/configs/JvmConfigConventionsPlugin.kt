package com.thefallendeveloper.indianmetro.configs

import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.LibraryExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinJvmCompile

class JvmConfigConventionsPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        val javaVersion = JavaVersion.toVersion(JvmConfigConstants.JVM_VERSION)
        val kotlinJvmTarget = JvmTarget.fromTarget(JvmConfigConstants.JVM_VERSION)

        target.pluginManager.withPlugin("com.android.application") {
            target.extensions.configure(ApplicationExtension::class.java) {
                compileOptions {
                    sourceCompatibility = javaVersion
                    targetCompatibility = javaVersion
                }
            }
        }

        target.pluginManager.withPlugin("com.android.library") {
            target.extensions.configure(LibraryExtension::class.java) {
                compileOptions {
                    sourceCompatibility = javaVersion
                    targetCompatibility = javaVersion
                }
            }
        }

        target.tasks.withType(KotlinJvmCompile::class.java).configureEach {
            if (name.contains("Android", ignoreCase = true)) {
                compilerOptions {
                    jvmTarget.set(kotlinJvmTarget)
                }
            }
        }
    }
}
