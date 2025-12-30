package com.iamkurtgoz.app.extensions

import org.gradle.api.Project
import org.gradle.kotlin.dsl.support.uppercaseFirstChar
import java.io.File

// ./gradlew createModule -PmodulePath=feature.test -PmoduleType=feature
internal fun Project.registerModuleCreatorTask() {
    tasks.register("createModule") {
        group = "module management"
        description = "Create a new module with screens and submodules"

        doLast {
            val modulePathWithDot = project.findProperty("modulePath") as? String
                ?: error("Please provide a modulePath property, e.g., -PmodulePath=feature.home.test")

            val moduleType = project.findProperty("moduleType") as? String
                ?: error("Please provide a moduleType property, e.g., -PmoduleType=feature or -PmoduleType=library")

            val isLibrary = moduleType == "library"
            val isFeature = moduleType == "feature"
            if (!isLibrary && !isFeature) {
                error("Please provide a isLibrary or isFeature property, -PmoduleType=feature or -PmoduleType=library")
            }

            createModuleStructure(
                modulePathWithDot = modulePathWithDot,
                isLibrary = isLibrary,
                isFeature = isFeature,
            )
        }
    }
}

private fun Project.createModuleStructure(
    modulePathWithDot: String,
    isLibrary: Boolean,
    isFeature: Boolean,
) {
    val splitPath = modulePathWithDot.split(".")
    val modulePathWithSeparator = splitPath.joinToString(File.separator)
    val moduleDir = File("${project.rootDir}/$modulePathWithSeparator")

    if (!moduleDir.exists()) {
        moduleDir.mkdirs()
    }

    println("modulePath: $modulePathWithSeparator")
    println("moduleDir: $moduleDir")

    createDirectoriesMainModule(
        moduleDir = moduleDir,
        modulePathWithSeparator = modulePathWithSeparator,
        isLibrary = isLibrary,
        isFeature = isFeature,
    )
    createDirectoriesDataModule(
        moduleDir = moduleDir,
        modulePathWithSeparator = modulePathWithSeparator,
        isLibrary = isLibrary,
    )
    createDirectoriesDomainModule(
        moduleDir = moduleDir,
        modulePathWithSeparator = modulePathWithSeparator,
        isLibrary = isLibrary,
    )

    updateSettingsFile(
        modulePathWithSeparator = modulePathWithSeparator,
        isLibrary = isLibrary,
    )
}

// Main Module
private fun Project.createDirectoriesMainModule(
    moduleDir: File,
    modulePathWithSeparator: String,
    isLibrary: Boolean,
    isFeature: Boolean,
) {
    val mainModuleDir = File(moduleDir, "src/main/kotlin/com/iamkurtgoz/$modulePathWithSeparator")
    val manifestFileDir = File(moduleDir, "src/main/")
    listOf(
        mainModuleDir,
        File(moduleDir, "src/androidTest/kotlin/com/iamkurtgoz/$modulePathWithSeparator"),
        File(moduleDir, "src/test/kotlin/com/iamkurtgoz/$modulePathWithSeparator"),
    ).forEach {
        println(it.absolutePath.plus(" --- > ${it.mkdirs()}"))
    }
    createMainModuleFiles(
        buildGradleFileDir = moduleDir,
        manifestFileDir = manifestFileDir,
        gitIgnoreFileDir = moduleDir,
        mainModuleDir = mainModuleDir,
        mainModulePathWithSeparator = modulePathWithSeparator,
        isLibrary = isLibrary,
        isFeature = isFeature,
    )
}

private fun Project.createMainModuleFiles(
    buildGradleFileDir: File,
    manifestFileDir: File,
    gitIgnoreFileDir: File,
    mainModuleDir: File,
    mainModulePathWithSeparator: String,
    isLibrary: Boolean,
    isFeature: Boolean,
) {
    // Build Gradle
    if (isLibrary) {
        File(buildGradleFileDir, "build.gradle.kts").writeText(
            """
            plugins {
                alias(libs.plugins.build.logic.android.library)
            }

            android {
                namespace = "com.iamkurtgoz.${mainModulePathWithSeparator.replace("/".toRegex(), ".")}"
                hilt.enableAggregatingTask = true
            }

            dependencies {
                // Projects
                implementation(projects.domain)

                // Projects - Core
                implementation(projects.core.common)
            }

            """.trimIndent(),
        )
    } else if (isFeature) {
        val namespace = "com.iamkurtgoz.${mainModulePathWithSeparator.replace("/".toRegex(), ".")}"
        val screenName = mainModuleDir.name.uppercaseFirstChar()
        File(buildGradleFileDir, "build.gradle.kts").writeText(
            """
            plugins {
                alias(libs.plugins.build.logic.android.feature)
                alias(libs.plugins.build.logic.android.library.compose)
            }

            android {
                namespace = "$namespace"
                hilt.enableAggregatingTask = true
            }

            dependencies {
                // Projects
                implementation(projects.domain)

                // Projects - Core
                implementation(projects.core.resources)
                implementation(projects.core.common)

                // Projects
                implementation(projects.${mainModulePathWithSeparator.replace("/".toRegex(), ".")}.domain)
            }

            """.trimIndent(),
        )

        // Compose Screen
        val composeScreenTemplateFile = File(project.rootDir, "config/templates/ComposeScreen.template")
        require(composeScreenTemplateFile.exists()) {
            "Template dosyası bulunamadı: ${composeScreenTemplateFile.absolutePath}"
        }
        val composeScreenTemplateContent = composeScreenTemplateFile.readText(Charsets.UTF_8)
            .replace("%__PACKAGE_NAME__%", namespace)
            .replace("%__SCREEN_NAME__%", screenName)
        val composeScreenOutputFile = File(mainModuleDir, "${screenName}Screen.kt")
        require(!composeScreenOutputFile.exists()) { "Error: File already exists: ${composeScreenOutputFile.absolutePath}" }
        composeScreenOutputFile.writeText(composeScreenTemplateContent)
        println("Created: ${composeScreenOutputFile.absolutePath}")

        // Compose Screen Content
        val composeScreenContentTemplateFile = File(project.rootDir, "config/templates/ComposeScreenContent.template")
        require(composeScreenContentTemplateFile.exists()) {
            "Template dosyası bulunamadı: ${composeScreenContentTemplateFile.absolutePath}"
        }
        val composeScreenContentTemplateContent = composeScreenContentTemplateFile.readText(Charsets.UTF_8)
            .replace("%__PACKAGE_NAME__%", namespace)
            .replace("%__SCREEN_NAME__%", screenName)
        val composeScreenContentOutputFile = File(mainModuleDir, "${screenName}ScreenContent.kt")
        require(!composeScreenContentOutputFile.exists()) { "Error: File already exists: ${composeScreenContentOutputFile.absolutePath}" }
        composeScreenContentOutputFile.writeText(composeScreenContentTemplateContent)
        println("Created: ${composeScreenContentOutputFile.absolutePath}")

        // Compose Screen Contract
        val composeScreenContractTemplateFile = File(project.rootDir, "config/templates/ComposeScreenContract.template")
        require(composeScreenContractTemplateFile.exists()) {
            "Template dosyası bulunamadı: ${composeScreenContractTemplateFile.absolutePath}"
        }
        val composeScreenContractTemplateContent = composeScreenContractTemplateFile.readText(Charsets.UTF_8)
            .replace("%__PACKAGE_NAME__%", namespace)
            .replace("%__SCREEN_NAME__%", screenName)
        val composeScreenContractOutputFile = File(mainModuleDir, "${screenName}ScreenContract.kt")
        require(!composeScreenContractOutputFile.exists()) { "Error: File already exists: ${composeScreenContractOutputFile.absolutePath}" }
        composeScreenContractOutputFile.writeText(composeScreenContractTemplateContent)
        println("Created: ${composeScreenContractOutputFile.absolutePath}")

        // Compose Screen ViewModel
        val composeScreenViewModelTemplateFile = File(project.rootDir, "config/templates/ComposeScreenViewModel.template")
        require(composeScreenViewModelTemplateFile.exists()) {
            "Template dosyası bulunamadı: ${composeScreenViewModelTemplateFile.absolutePath}"
        }
        val composeScreenViewModelTemplateContent = composeScreenViewModelTemplateFile.readText(Charsets.UTF_8)
            .replace("%__PACKAGE_NAME__%", namespace)
            .replace("%__SCREEN_NAME__%", screenName)
        val composeScreenViewModelOutputFile = File(mainModuleDir, "${screenName}ViewModel.kt")
        require(!composeScreenViewModelOutputFile.exists()) { "Error: File already exists: ${composeScreenViewModelOutputFile.absolutePath}" }
        composeScreenViewModelOutputFile.writeText(composeScreenViewModelTemplateContent)
        println("Created: ${composeScreenViewModelOutputFile.absolutePath}")

        // Compose Screen Navigation
        val composeScreenNavigationTemplateFile = File(project.rootDir, "config/templates/ComposeScreenNavigation.template")
        require(composeScreenNavigationTemplateFile.exists()) {
            "Template dosyası bulunamadı: ${composeScreenNavigationTemplateFile.absolutePath}"
        }
        val composeScreenNavigationTemplateContent = composeScreenNavigationTemplateFile.readText(Charsets.UTF_8)
            .replace("%__PACKAGE_NAME__%", namespace)
            .replace("%__SCREEN_NAME_START_SMALL_CHARS__%", screenName.lowercase())
            .replace("%__SCREEN_NAME__%", screenName)
        val composeScreenNavigationOutputFolder = File(mainModuleDir, "navigation")
        if (!composeScreenNavigationOutputFolder.exists()) {
            composeScreenNavigationOutputFolder.mkdirs()
        }
        val composeScreenNavigationOutputFile = File(composeScreenNavigationOutputFolder, "${screenName}Navigation.kt")
        require(!composeScreenNavigationOutputFile.exists()) { "Error: File already exists: ${composeScreenNavigationOutputFile.absolutePath}" }
        composeScreenNavigationOutputFile.writeText(composeScreenNavigationTemplateContent)
        println("Created: ${composeScreenNavigationOutputFile.absolutePath}")
    }

    // Manifest File
    File(manifestFileDir, "AndroidManifest.xml").writeText(
        """
        <?xml version="1.0" encoding="utf-8"?>
        <manifest>
        </manifest>

        """.trimIndent(),
    )

    // Gitignore
    File(gitIgnoreFileDir, ".gitignore").writeText("/build")
}

// Data Module
private fun createDirectoriesDataModule(
    moduleDir: File,
    modulePathWithSeparator: String,
    isLibrary: Boolean,
) {
    if (isLibrary) return

    val dataModuleDir = File(moduleDir, "data/src/main/kotlin/com/iamkurtgoz/$modulePathWithSeparator/data")
    val buildGradleFileDir = File(moduleDir, "data")
    val manifestFileDir = File(moduleDir, "data/src/main/")
    val gitIgnoreFileDir = File(moduleDir, "data")
    listOf(
        dataModuleDir,
        File(moduleDir, "data/src/androidTest/kotlin/com/iamkurtgoz/$modulePathWithSeparator/data"),
        File(moduleDir, "data/src/test/kotlin/com/iamkurtgoz/$modulePathWithSeparator/data"),
    ).forEach {
        println(it.absolutePath.plus(" --- > ${it.mkdirs()}"))
    }

    createDataModuleFiles(
        moduleDir = moduleDir,
        buildGradleFileDir = buildGradleFileDir,
        manifestFileDir = manifestFileDir,
        gitIgnoreFileDir = gitIgnoreFileDir,
        mainModulePathWithSeparator = modulePathWithSeparator,
    )
}

private fun createDataModuleFiles(
    moduleDir: File,
    buildGradleFileDir: File,
    manifestFileDir: File,
    gitIgnoreFileDir: File,
    mainModulePathWithSeparator: String,
) {
    // Build Gradle
    File(buildGradleFileDir, "build.gradle.kts").writeText(
        """
            plugins {
                alias(libs.plugins.build.logic.android.feature)
                alias(libs.plugins.build.logic.android.library.compose)
            }

            android {
                namespace = "com.iamkurtgoz.${mainModulePathWithSeparator.replace("/".toRegex(), ".")}.data"
                hilt.enableAggregatingTask = true
            }

            dependencies {
                // Projects
                implementation(projects.domain)

                // Projects - Core
                implementation(projects.core.resources)
                implementation(projects.core.common)

                // Projects
                implementation(projects.${mainModulePathWithSeparator.replace("/".toRegex(), ".").replace("com.iamkurtgoz.", "")}.domain)
            }

        """.trimIndent(),
    )

    // Manifest
    File(manifestFileDir, "AndroidManifest.xml").writeText(
        """
        <?xml version="1.0" encoding="utf-8"?>
        <manifest>
        </manifest>

        """.trimIndent(),
    )

    // Gitignore
    File(gitIgnoreFileDir, ".gitignore").writeText("/build")

    // --- Örnek Test Dosyaları ---
    // Unit Test
    val dataTestDir = File(moduleDir, "data/src/test/kotlin/com/iamkurtgoz/$mainModulePathWithSeparator/data")
    if (!dataTestDir.exists()) dataTestDir.mkdirs()
    val sampleTestFile = File(dataTestDir, "DataModuleTest.kt")
    if (!sampleTestFile.exists()) {
        sampleTestFile.writeText(
            """
            package com.iamkurtgoz.${mainModulePathWithSeparator.replace(File.separator, ".")}.data

            import org.junit.Test
            import org.junit.Assert.assertTrue

            class DataModuleTest {
                @Test
                fun testExample() {
                    assertTrue(true)
                }
            }

            """.trimIndent(),
        )
        println("Created: ${sampleTestFile.absolutePath}")
    }

    // Android Instrumented Test
    val dataAndroidTestDir = File(moduleDir, "data/src/androidTest/kotlin/com/iamkurtgoz/$mainModulePathWithSeparator/data")
    if (!dataAndroidTestDir.exists()) dataAndroidTestDir.mkdirs()
    val sampleAndroidTestFile = File(dataAndroidTestDir, "DataModuleInstrumentedTest.kt")
    if (!sampleAndroidTestFile.exists()) {
        sampleAndroidTestFile.writeText(
            """
            package com.iamkurtgoz.${mainModulePathWithSeparator.replace(File.separator, ".")}.data

            import androidx.test.platform.app.InstrumentationRegistry
            import androidx.test.ext.junit.runners.AndroidJUnit4
            import org.junit.Test
            import org.junit.runner.RunWith
            import org.junit.Assert.assertEquals

            @RunWith(AndroidJUnit4::class)
            class DataModuleInstrumentedTest {
                @Test
                fun useAppContext() {
                    val appContext = InstrumentationRegistry.getInstrumentation().targetContext
                    assertEquals("com.iamkurtgoz.${mainModulePathWithSeparator.replace(File.separator, ".")}.data", appContext.packageName)
                }
            }

            """.trimIndent(),
        )
        println("Created: ${sampleAndroidTestFile.absolutePath}")
    }
}

// Domain Module
private fun createDirectoriesDomainModule(
    moduleDir: File,
    modulePathWithSeparator: String,
    isLibrary: Boolean,
) {
    if (isLibrary) return

    val domainModuleDir = File(moduleDir, "domain/src/main/kotlin/com/iamkurtgoz/$modulePathWithSeparator/domain")
    val buildGradleFileDir = File(moduleDir, "domain")
    val manifestFileDir = File(moduleDir, "domain/src/main/")
    val gitIgnoreFileDir = File(moduleDir, "domain")
    listOf(
        domainModuleDir,
        File(moduleDir, "domain/src/androidTest/kotlin/com/iamkurtgoz/$modulePathWithSeparator/domain"),
        File(moduleDir, "domain/src/test/kotlin/com/iamkurtgoz/$modulePathWithSeparator/domain"),
    ).forEach {
        println(it.absolutePath.plus(" --- > ${it.mkdirs()}"))
    }

    createDomainModuleFiles(
        moduleDir = moduleDir,
        buildGradleFileDir = buildGradleFileDir,
        manifestFileDir = manifestFileDir,
        gitIgnoreFileDir = gitIgnoreFileDir,
        mainModulePathWithSeparator = modulePathWithSeparator,
    )
}

private fun createDomainModuleFiles(
    moduleDir: File,
    buildGradleFileDir: File,
    manifestFileDir: File,
    gitIgnoreFileDir: File,
    mainModulePathWithSeparator: String,
) {
    // Build Gradle
    File(buildGradleFileDir, "build.gradle.kts").writeText(
        """
            plugins {
                alias(libs.plugins.build.logic.android.library)
            }

            android {
                namespace = "com.iamkurtgoz.${mainModulePathWithSeparator.replace("/".toRegex(), ".")}.domain"
                hilt.enableAggregatingTask = true
            }

            dependencies {
                // Projects
                implementation(projects.domain)

                // Projects - Core
                implementation(projects.core.common)
            }

        """.trimIndent(),
    )

    // Manifest
    File(manifestFileDir, "AndroidManifest.xml").writeText(
        """
        <?xml version="1.0" encoding="utf-8"?>
        <manifest>
        </manifest>

        """.trimIndent(),
    )

    // Gitignore
    File(gitIgnoreFileDir, ".gitignore").writeText("/build")

    // --- Örnek Test Dosyaları ---
    // Unit Test
    val domainTestDir = File(moduleDir, "domain/src/test/kotlin/com/iamkurtgoz/$mainModulePathWithSeparator/domain")
    if (!domainTestDir.exists()) domainTestDir.mkdirs()
    val sampleTestFile = File(domainTestDir, "DomainModuleTest.kt")
    if (!sampleTestFile.exists()) {
        sampleTestFile.writeText(
            """
            package com.iamkurtgoz.${mainModulePathWithSeparator.replace(File.separator, ".")}.domain

            import org.junit.Test
            import org.junit.Assert.assertTrue

            class DomainModuleTest {
                @Test
                fun testExample() {
                    assertTrue(true)
                }
            }

            """.trimIndent(),
        )
        println("Created: ${sampleTestFile.absolutePath}")
    }

    // Android Instrumented Test
    val domainAndroidTestDir = File(moduleDir, "domain/src/androidTest/kotlin/com/iamkurtgoz/$mainModulePathWithSeparator/domain")
    if (!domainAndroidTestDir.exists()) domainAndroidTestDir.mkdirs()
    val sampleAndroidTestFile = File(domainAndroidTestDir, "DomainModuleInstrumentedTest.kt")
    if (!sampleAndroidTestFile.exists()) {
        sampleAndroidTestFile.writeText(
            """
            package com.iamkurtgoz.${mainModulePathWithSeparator.replace(File.separator, ".")}.domain

            import androidx.test.platform.app.InstrumentationRegistry
            import androidx.test.ext.junit.runners.AndroidJUnit4
            import org.junit.Test
            import org.junit.runner.RunWith
            import org.junit.Assert.assertEquals

            @RunWith(AndroidJUnit4::class)
            class DomainModuleInstrumentedTest {
                @Test
                fun useAppContext() {
                    val appContext = InstrumentationRegistry.getInstrumentation().targetContext
                    assertEquals("com.iamkurtgoz.${mainModulePathWithSeparator.replace(File.separator, ".")}.domain", appContext.packageName)
                }
            }

            """.trimIndent(),
        )
        println("Created: ${sampleAndroidTestFile.absolutePath}")
    }
}

private fun Project.updateSettingsFile(
    modulePathWithSeparator: String,
    isLibrary: Boolean,
) {
    val settingsFile = File(rootProject.projectDir, "settings.gradle.kts")
    val settingsContent = settingsFile.readText()
    var moduleRegistration = """
        include(":${modulePathWithSeparator.replace("/".toRegex(), ":")}")
        include(":${modulePathWithSeparator.replace("/".toRegex(), ":")}:data")
        include(":${modulePathWithSeparator.replace("/".toRegex(), ":")}:domain")

    """.trimIndent()

    if (isLibrary) {
        moduleRegistration = """
        include(":${modulePathWithSeparator.replace("/".toRegex(), ":")}")

        """.trimIndent()
    }

    settingsFile.writeText(settingsContent + moduleRegistration)
}
