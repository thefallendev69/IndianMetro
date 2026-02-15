package com.thefallendeveloper.indianmetro.linting

import org.gradle.api.Plugin
import org.gradle.api.Project

class LintSuitePlugin : Plugin<Project> {
    override fun apply(target: Project) {
        if (target != target.rootProject) return

        val runLintSuite = target.tasks.register("runLintSuite", RunLintSuiteTask::class.java)
        runLintSuite.configure {
            rootProjectDirectory.set(target.layout.projectDirectory)
        }
    }
}
