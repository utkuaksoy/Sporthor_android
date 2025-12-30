import dev.iurysouza.modulegraph.ModuleType
import dev.iurysouza.modulegraph.Theme

plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.compose.compiler) apply false
    alias(libs.plugins.jetbrains.kotlin.android) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.kotlin.jvm) apply false
    alias(libs.plugins.kotlin.serialization) apply false
    alias(libs.plugins.kotlin.parcelize) apply false
    alias(libs.plugins.hilt) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.roborazzi) apply false
    alias(libs.plugins.secrets) apply false
    alias(libs.plugins.detekt) apply false
    alias(libs.plugins.ktlint) apply false
    alias(libs.plugins.spotless)
    alias(libs.plugins.google.services) apply false
    alias(libs.plugins.firebase.crashlytics) apply false
    alias(libs.plugins.dev.iurysouza.modulegraph)
    alias(libs.plugins.jraska.module.graph.assertion) apply true
}

moduleGraphConfig {
    readmePath.set("./README.md")
    heading = "### Module Graph"
    excludedConfigurationsRegex.set(".*test.*")
    rootModulesRegex.set(".*(app).*")
    focusedModulesRegex.set(".*(app).*")
    setStyleByModuleType.set(true)
    theme.set(
        Theme.BASE(
            themeVariables = mapOf(
                "primaryTextColor" to "#E0E0E0",
                "primaryColor" to "#374151",
                "primaryBorderColor" to "#1F2937",
                "tertiaryColor" to "#111827",
                "lineColor" to "#10B981",
                "fontSize" to "14px",
            ),
            focusColor = "#3B82F6",
            moduleTypes = listOf(
                ModuleType.Custom(id = "app.compose", color = "#9333EA"),
                ModuleType.AndroidApp("#3B82F6"),
                ModuleType.AndroidLibrary("#EF4444"),
            ),
        ),
    )
}

// Module Generate
// To Use: ./gradlew createModule -PmodulePath= -PmoduleType=
// Example: ./gradlew createModule -PmodulePath=feature.home.dashboard -PmoduleType=feature
