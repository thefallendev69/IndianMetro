import org.gradle.api.artifacts.VersionCatalogsExtension

plugins {
    `kotlin-dsl`
    `java-gradle-plugin`
}

repositories {
    gradlePluginPortal()
    mavenCentral()
    google()
}

val libsCatalog = extensions.getByType(VersionCatalogsExtension::class.java).named("libs")

dependencies {
    implementation(libsCatalog.findLibrary("ktlintGradlePlugin").get())
    implementation(libsCatalog.findLibrary("detektGradlePlugin").get())
}

gradlePlugin {
    plugins {
        register("indianmetroLintConventions") {
            id = "com.thefallendeveloper.indianmetro.lint-conventions"
            implementationClass = "com.thefallendeveloper.indianmetro.linting.LintConventionsPlugin"
        }
    }
}
