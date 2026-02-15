plugins {
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.androidLibrary) apply false
    alias(libs.plugins.composeMultiplatform) apply false
    alias(libs.plugins.composeCompiler) apply false
    alias(libs.plugins.kotlinMultiplatform) apply false
    alias(libs.plugins.androidKotlinMultiplatformLibrary) apply false
    alias(libs.plugins.ktlint) apply false
    alias(libs.plugins.detekt) apply false
}

// 2. Add the plugin dependency to the root project's *buildscript* classpath
buildscript {
    repositories {
        mavenCentral()
        google()
    }
}

allprojects {
    apply(plugin = "org.jlleitschuh.gradle.ktlint")

    configure<org.jlleitschuh.gradle.ktlint.KtlintExtension> {
        verbose.set(true)
        outputToConsole.set(true)
        android.set(true) // Required if using Android targets

        filter {
            exclude("**/generated/**") // Avoid linting generated KMP code
            include("**/src/**")
        }
    }
}
