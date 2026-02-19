@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.indianmetroLintConventions)
}

kotlin {
    jvm()

    jvmToolchain(11)
}
