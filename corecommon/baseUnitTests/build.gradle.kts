import org.gradle.api.tasks.testing.Test

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.indianmetroLintConventions)
}

kotlin {
    jvm()

    jvmToolchain(11)

    sourceSets {
        commonMain.dependencies {
            api(libs.kotlinx.coroutines.test)
            api(libs.mockk)
            api(libs.kotlin.test)
        }
        jvmTest.dependencies {
            implementation(libs.junit5.api)
            runtimeOnly(libs.junit5.engine)
        }
    }
}

tasks.withType<Test>().configureEach {
    useJUnitPlatform()
}
