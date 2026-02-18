plugins {
    alias(libs.plugins.kotlinJvm)
    alias(libs.plugins.indianmetroLintConventions)
}

kotlin {
    jvmToolchain(11)
}

dependencies {
    api(libs.junit5.api)
    api(libs.mockk)
    api(libs.kotlinx.coroutines.test)
    testRuntimeOnly(libs.junit5.engine)
}

tasks.test {
    useJUnitPlatform()
}
