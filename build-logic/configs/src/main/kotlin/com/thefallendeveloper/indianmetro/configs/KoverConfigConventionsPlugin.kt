package com.thefallendeveloper.indianmetro.configs

import kotlinx.kover.gradle.plugin.dsl.KoverProjectExtension
import org.gradle.api.Plugin
import org.gradle.api.Project

class KoverConfigConventionsPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        target.subprojects {
            pluginManager.withPlugin("org.jetbrains.kotlinx.kover") {
                extensions.configure(KoverProjectExtension::class.java) {
                    reports {
                        filters {
                            excludes {
                                packages(
                                    "*.di",
                                    "*.di.*",
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
