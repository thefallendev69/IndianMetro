@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.indianmetroLintConventions)
}

kotlin {
    jvm()

    jvmToolchain(11)

    sourceSets {
        commonMain.dependencies {
            api(libs.kotlin.test)
            api(libs.kotlinx.coroutines.test)
            api(libs.turbine)
            api(libs.kotest.assertions.core)
            api(libs.kotest.framework.engine)
        }

        jvmMain.dependencies {
            api(libs.kotest.runner.junit5)
        }
    }
}
