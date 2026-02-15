package com.thefallendeveloper.indianmetro.linting

import org.gradle.api.DefaultTask
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.tasks.InputDirectory
import org.gradle.api.tasks.TaskAction
import org.gradle.tooling.BuildException
import org.gradle.tooling.GradleConnector
import org.gradle.work.DisableCachingByDefault

@DisableCachingByDefault(because = "Runs nested Gradle builds via Tooling API.")
abstract class RunLintSuiteTask : DefaultTask() {
    @get:InputDirectory
    abstract val rootProjectDirectory: DirectoryProperty

    init {
        group = "verification"
        description = "Runs ktlintCheck and detekt. If ktlintCheck fails, runs ktlintFormat and retries."
        notCompatibleWithConfigurationCache("Uses Tooling API to run nested Gradle builds.")
    }

    @TaskAction
    fun executeSuite() {
        try {
            runBuild("ktlintCheck")
        } catch (_: BuildException) {
            logger.lifecycle("ktlintCheck failed. Running ktlintFormat, then retrying ktlintCheck.")
            runBuild("ktlintFormat")
            runBuild("ktlintCheck")
        }

        runBuild("detekt")
    }

    private fun runBuild(vararg taskNames: String) {
        GradleConnector.newConnector()
            .forProjectDirectory(rootProjectDirectory.get().asFile)
            .connect()
            .use { connection ->
                connection.newBuild()
                    .forTasks(*taskNames)
                    .setStandardOutput(System.out)
                    .setStandardError(System.err)
                    .run()
            }
    }
}
