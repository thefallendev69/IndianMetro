import org.gradle.api.tasks.testing.Test

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
        }

        jvmMain.dependencies {
            api(libs.mockk)
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
