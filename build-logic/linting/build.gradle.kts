plugins {
    `kotlin-dsl`
    `java-gradle-plugin`
}

repositories {
    gradlePluginPortal()
    mavenCentral()
    google()
}

dependencies {
    implementation("org.jlleitschuh.gradle:ktlint-gradle:14.0.1")
    implementation("io.gitlab.arturbosch.detekt:detekt-gradle-plugin:1.23.8")
}

gradlePlugin {
    plugins {
        register("indianmetroLintConventions") {
            id = "com.thefallendeveloper.indianmetro.lint-conventions"
            implementationClass = "com.thefallendeveloper.indianmetro.linting.LintConventionsPlugin"
        }
    }
}
