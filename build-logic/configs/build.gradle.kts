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
    implementation(libsCatalog.findLibrary("kotlin-gradle-plugin").get())
    implementation(libsCatalog.findLibrary("android-gradle-plugin").get())
}

gradlePlugin {
    plugins {
        register("indianmetroJvmConfigConventions") {
            id = "com.thefallendeveloper.indianmetro.jvm-config-conventions"
            implementationClass = "com.thefallendeveloper.indianmetro.configs.JvmConfigConventionsPlugin"
        }
    }
}
