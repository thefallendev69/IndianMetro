import groovy.json.JsonSlurper
import org.gradle.api.DefaultTask
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompilationTask

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.indianmetroLintConventions)
}

abstract class GenerateDesignTokensTask : DefaultTask() {
    @get:InputFile
    abstract val tokenJsonFile: RegularFileProperty

    @get:OutputDirectory
    abstract val outputDirectory: DirectoryProperty

    @TaskAction
    fun generate() {
        val root = parseJson(tokenJsonFile.get().asFile.readText())

        val outputDir = outputDirectory.get().asFile
        outputDir.mkdirs()

        val colorsPackagePath = "com/thefallendeveloper/indianmetro/designsystem/tokens"
        val colorsFile = outputDir.resolve("$colorsPackagePath/ColorTokens.kt")
        val typographyFile = outputDir.resolve("$colorsPackagePath/TypographyTokenValues.kt")
        colorsFile.parentFile.mkdirs()
        colorsFile.writeText(buildColorTokensFile(root))
        typographyFile.writeText(buildTypographyTokensFile(root))
    }

    @Suppress("UNCHECKED_CAST")
    private fun parseJson(content: String): Map<String, Any> = JsonSlurper().parseText(content) as Map<String, Any>

    @Suppress("UNCHECKED_CAST")
    private fun mapAt(
        root: Map<String, Any>,
        vararg path: String
    ): Map<String, Any> {
        var current: Any = root
        path.forEach { key ->
            current = (current as Map<String, Any>).getValue(key)
        }
        return current as Map<String, Any>
    }

    private fun tokenValue(
        root: Map<String, Any>,
        vararg path: String
    ): String = mapAt(root, *path).getValue("value").toString()

    private fun resolveColorValue(
        root: Map<String, Any>,
        value: String
    ): String {
        if (value.startsWith("{") && value.endsWith("}")) {
            val refPath =
                value
                    .removePrefix("{")
                    .removeSuffix("}")
                    .removeSuffix(".value")
                    .split(".")
            val fullPath = listOf("tokens") + refPath
            return tokenValue(root, *fullPath.toTypedArray())
        }
        return value
    }

    private fun colorLiteral(hex: String): String {
        val cleaned = hex.removePrefix("#").uppercase()
        return when (cleaned.length) {
            6 -> "Color(0xFF$cleaned)"
            8 -> "Color(0x$cleaned)"
            else -> error("Unsupported color format: $hex")
        }
    }

    private fun buildColorTokensFile(root: Map<String, Any>): String {
        val brandPrimaryStart = colorLiteral(tokenValue(root, "tokens", "color", "brand", "primary", "start"))
        val brandPrimaryEnd = colorLiteral(tokenValue(root, "tokens", "color", "brand", "primary", "end"))

        val metroRed = colorLiteral(tokenValue(root, "tokens", "color", "metro_lines", "red"))
        val metroYellow = colorLiteral(tokenValue(root, "tokens", "color", "metro_lines", "yellow"))
        val metroBlue = colorLiteral(tokenValue(root, "tokens", "color", "metro_lines", "blue"))
        val metroGreen = colorLiteral(tokenValue(root, "tokens", "color", "metro_lines", "green"))

        val slate50 = colorLiteral(tokenValue(root, "tokens", "color", "neutral", "slate_50"))
        val slate200 = colorLiteral(tokenValue(root, "tokens", "color", "neutral", "slate_200"))
        val slate400 = colorLiteral(tokenValue(root, "tokens", "color", "neutral", "slate_400"))
        val slate500 = colorLiteral(tokenValue(root, "tokens", "color", "neutral", "slate_500"))
        val slate700 = colorLiteral(tokenValue(root, "tokens", "color", "neutral", "slate_700"))
        val slate900 = colorLiteral(tokenValue(root, "tokens", "color", "neutral", "slate_900"))
        val slate950 = colorLiteral(tokenValue(root, "tokens", "color", "neutral", "slate_950"))
        val white = colorLiteral(tokenValue(root, "tokens", "color", "neutral", "white"))

        val lightBackground =
            colorLiteral(
                resolveColorValue(
                    root,
                    tokenValue(root, "tokens", "color", "semantic", "background", "light"),
                ),
            )
        val darkBackground =
            colorLiteral(
                resolveColorValue(
                    root,
                    tokenValue(root, "tokens", "color", "semantic", "background", "dark"),
                ),
            )
        val lightSurface =
            colorLiteral(
                resolveColorValue(
                    root,
                    tokenValue(root, "tokens", "color", "semantic", "surface", "light"),
                ),
            )
        val darkSurface =
            colorLiteral(
                resolveColorValue(
                    root,
                    tokenValue(root, "tokens", "color", "semantic", "surface", "dark"),
                ),
            )
        val lightTextPrimary =
            colorLiteral(
                resolveColorValue(
                    root,
                    tokenValue(root, "tokens", "color", "semantic", "text", "primary", "light"),
                ),
            )
        val darkTextPrimary =
            colorLiteral(
                resolveColorValue(
                    root,
                    tokenValue(root, "tokens", "color", "semantic", "text", "primary", "dark"),
                ),
            )
        val lightTextSecondary =
            colorLiteral(
                resolveColorValue(
                    root,
                    tokenValue(root, "tokens", "color", "semantic", "text", "secondary", "light"),
                ),
            )
        val darkTextSecondary =
            colorLiteral(
                resolveColorValue(
                    root,
                    tokenValue(root, "tokens", "color", "semantic", "text", "secondary", "dark"),
                ),
            )
        val lightBorderDefault =
            colorLiteral(
                resolveColorValue(
                    root,
                    tokenValue(root, "tokens", "color", "semantic", "border", "default", "light"),
                ),
            )
        val darkBorderDefault =
            colorLiteral(
                resolveColorValue(
                    root,
                    tokenValue(root, "tokens", "color", "semantic", "border", "default", "dark"),
                ),
            )

        return """
            package com.thefallendeveloper.indianmetro.designsystem.tokens
            
            import androidx.compose.ui.graphics.Color
            
            object ColorTokens {
                object Brand {
                    val primaryStart: Color = $brandPrimaryStart
                    val primaryEnd: Color = $brandPrimaryEnd
                }
            
                object MetroLines {
                    val red: Color = $metroRed
                    val yellow: Color = $metroYellow
                    val blue: Color = $metroBlue
                    val green: Color = $metroGreen
                }
            
                object Neutral {
                    val slate50: Color = $slate50
                    val slate200: Color = $slate200
                    val slate400: Color = $slate400
                    val slate500: Color = $slate500
                    val slate700: Color = $slate700
                    val slate900: Color = $slate900
                    val slate950: Color = $slate950
                    val white: Color = $white
                }
            
                object Semantic {
                    object Light {
                        val background: Color = $lightBackground
                        val surface: Color = $lightSurface
                        val textPrimary: Color = $lightTextPrimary
                        val textSecondary: Color = $lightTextSecondary
                        val borderDefault: Color = $lightBorderDefault
                    }
            
                    object Dark {
                        val background: Color = $darkBackground
                        val surface: Color = $darkSurface
                        val textPrimary: Color = $darkTextPrimary
                        val textSecondary: Color = $darkTextSecondary
                        val borderDefault: Color = $darkBorderDefault
                    }
                }
            }
            """.trimIndent() + "\n"
    }

    private fun buildTypographyTokensFile(root: Map<String, Any>): String {
        fun px(value: String): Int = value.removeSuffix("px").trim().toInt()

        fun em(value: String): Double = value.removeSuffix("em").trim().toDouble()

        fun weight(value: String): Int = value.toInt()

        val h1Size = px(tokenValue(root, "tokens", "typography", "h1", "fontSize"))
        val h1Weight = weight(tokenValue(root, "tokens", "typography", "h1", "fontWeight"))
        val h1LetterSpacing = em(tokenValue(root, "tokens", "typography", "h1", "letterSpacing"))

        val h2Size = px(tokenValue(root, "tokens", "typography", "h2", "fontSize"))
        val h2Weight = weight(tokenValue(root, "tokens", "typography", "h2", "fontWeight"))

        val h3Size = px(tokenValue(root, "tokens", "typography", "h3", "fontSize"))
        val h3Weight = weight(tokenValue(root, "tokens", "typography", "h3", "fontWeight"))

        val bodySize = px(tokenValue(root, "tokens", "typography", "body", "fontSize"))
        val bodyWeight = weight(tokenValue(root, "tokens", "typography", "body", "fontWeight"))

        val labelSize = px(tokenValue(root, "tokens", "typography", "label", "fontSize"))
        val labelWeight = weight(tokenValue(root, "tokens", "typography", "label", "fontWeight"))
        val labelLetterSpacing = em(tokenValue(root, "tokens", "typography", "label", "letterSpacing"))
        val labelTransform = tokenValue(root, "tokens", "typography", "label", "textTransform")

        return """
            package com.thefallendeveloper.indianmetro.designsystem.tokens
            
            object TypographyTokenValues {
                const val h1FontSizePx: Int = $h1Size
                const val h1FontWeight: Int = $h1Weight
                const val h1LetterSpacingEm: Double = $h1LetterSpacing
            
                const val h2FontSizePx: Int = $h2Size
                const val h2FontWeight: Int = $h2Weight
            
                const val h3FontSizePx: Int = $h3Size
                const val h3FontWeight: Int = $h3Weight
            
                const val bodyFontSizePx: Int = $bodySize
                const val bodyFontWeight: Int = $bodyWeight
            
                const val labelFontSizePx: Int = $labelSize
                const val labelFontWeight: Int = $labelWeight
                const val labelLetterSpacingEm: Double = $labelLetterSpacing
                const val labelTextTransform: String = "$labelTransform"
            }
            """.trimIndent() + "\n"
    }
}

val generateDesignTokens =
    tasks.register("generateDesignTokens", GenerateDesignTokensTask::class.java) {
        tokenJsonFile.set(layout.projectDirectory.file("design-language/metro-ui-design-system.json"))
        outputDirectory.set(layout.buildDirectory.dir("generated/designTokens/src/commonMain/kotlin"))
    }

kotlin {
    androidTarget {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }

    listOf(
        iosArm64(),
        iosSimulatorArm64(),
    )

    sourceSets {
        commonMain {
            kotlin.srcDir(layout.buildDirectory.dir("generated/designTokens/src/commonMain/kotlin"))

            dependencies {
                implementation(compose.runtime)
                implementation(compose.foundation)
                implementation(compose.material3)
                implementation(compose.ui)
                implementation(compose.components.resources)
                implementation(compose.components.uiToolingPreview)
            }
        }
    }
}

tasks.withType(KotlinCompilationTask::class.java).configureEach {
    dependsOn(generateDesignTokens)
}

tasks.matching { task -> task.name.startsWith("runKtlint") || task.name == "detekt" }.configureEach {
    dependsOn(generateDesignTokens)
}

android {
    namespace = "com.thefallendeveloper.indianmetro.designsystem"
    compileSdk =
        libs.versions.android.compileSdk
            .get()
            .toInt()

    defaultConfig {
        minSdk =
            libs.versions.android.minSdk
                .get()
                .toInt()
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}
