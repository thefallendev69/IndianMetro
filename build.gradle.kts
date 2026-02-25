import kotlinx.kover.gradle.plugin.dsl.KoverProjectExtension

plugins {
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.androidLibrary) apply false
    alias(libs.plugins.composeMultiplatform) apply false
    alias(libs.plugins.composeCompiler) apply false
    alias(libs.plugins.kotlinMultiplatform) apply false
    alias(libs.plugins.androidKotlinMultiplatformLibrary) apply false
    alias(libs.plugins.kover) apply false
    alias(libs.plugins.detekt) apply false
    alias(libs.plugins.indianmetroLintSuite)
}

apply(from = "$rootDir/gradle/ij-kotlin-mpp-test-logger-workaround.gradle")

val koverPluginId = libs.plugins.kover.get().pluginId

subprojects {
    pluginManager.withPlugin("org.jetbrains.kotlin.multiplatform") {
        afterEvaluate {
            val hasBaseTestDependency =
                configurations.any { configuration ->
                    configuration.dependencies.any { dependency ->
                        dependency is org.gradle.api.artifacts.ProjectDependency &&
                            dependency.dependencyProject.path == ":corecommon:baseTest"
                    }
                }

            if (hasBaseTestDependency && !pluginManager.hasPlugin(koverPluginId)) {
                pluginManager.apply(koverPluginId)
            }
        }
    }

    pluginManager.withPlugin(koverPluginId) {
        extensions.configure<KoverProjectExtension> {
            reports {
                filters {
                    excludes {
                        packages(
                            "*.di",
                            "*.di.*",
                        )
                    }
                }
            }
        }
    }
}
