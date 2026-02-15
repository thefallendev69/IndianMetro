package com.thefallendeveloper.indianmetro.linting

import io.gitlab.arturbosch.detekt.Detekt
import io.gitlab.arturbosch.detekt.extensions.DetektExtension
import org.gradle.api.Action
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.provider.Property
import org.jlleitschuh.gradle.ktlint.KtlintExtension
import org.jlleitschuh.gradle.ktlint.KtlintPlugin

class LintConventionsPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        target.pluginManager.apply(KtlintPlugin::class.java)
        target.pluginManager.apply("io.gitlab.arturbosch.detekt")

        target.extensions.configure(KtlintExtension::class.java) {
            // KtLint extension accessors are not stable across plugin versions.
            setBooleanProperty(this, "getVerbose", true)
            setBooleanProperty(this, "getOutputToConsole", true)
            setBooleanProperty(this, "getAndroid", true)
            configureFilter(this, includePattern = "**/src/**", excludePattern = "**/generated/**")
        }

        target.extensions.configure(DetektExtension::class.java) {
            buildUponDefaultConfig = true
            allRules = false
            val detektConfigFile = target.rootProject.file("config/detekt/detekt.yml")
            if (detektConfigFile.exists()) {
                config.setFrom(detektConfigFile)
            }
        }

        target.tasks.withType(Detekt::class.java).configureEach {
            setSource(target.files("src/commonMain/kotlin", "src/androidMain/kotlin", "src/iosMain/kotlin"))
            include("**/*.kt", "**/*.kts")
            exclude("**/build/**", "**/generated/**", "**/*.android.kt", "**/*.ios.kt")
        }
    }

    @Suppress("UNCHECKED_CAST")
    private fun setBooleanProperty(
        extension: Any,
        getterName: String,
        value: Boolean,
    ) {
        val property = extension.javaClass.getMethod(getterName).invoke(extension) as Property<Boolean>
        property.set(value)
    }

    private fun configureFilter(
        extension: Any,
        includePattern: String,
        excludePattern: String,
    ) {
        val filterMethod =
            extension.javaClass.methods.firstOrNull { method ->
                method.name == "filter" &&
                    method.parameterCount == 1 &&
                    Action::class.java.isAssignableFrom(method.parameterTypes[0])
            } ?: return

        filterMethod.invoke(
            extension,
            object : Action<Any> {
                override fun execute(filter: Any) {
                    invokeStringMethod(filter, "exclude", excludePattern)
                    invokeStringMethod(filter, "include", includePattern)
                }
            },
        )
    }

    private fun invokeStringMethod(
        target: Any,
        methodName: String,
        value: String,
    ) {
        val method =
            target.javaClass.methods.firstOrNull { candidate ->
                candidate.name == methodName &&
                    candidate.parameterCount == 1 &&
                    candidate.parameterTypes[0] == String::class.java
            } ?: return

        method.invoke(target, value)
    }
}
