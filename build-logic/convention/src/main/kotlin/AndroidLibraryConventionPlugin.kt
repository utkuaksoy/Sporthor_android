import com.android.build.gradle.LibraryExtension
import com.iamkurtgoz.app.extensions.configureKotlinAndroid
import com.iamkurtgoz.app.utils.androidTestImplementation
import com.iamkurtgoz.app.utils.implementation
import com.iamkurtgoz.app.utils.libs
import com.iamkurtgoz.app.utils.testImplementation
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.kotlin

class AndroidLibraryConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.android.library")
                apply("org.jetbrains.kotlin.android")
                apply("org.jetbrains.kotlin.plugin.serialization")
                apply("org.jetbrains.kotlin.plugin.parcelize")
                apply("com.iamkurtgoz.sub.timber")
                apply("com.iamkurtgoz.sub.hilt")
                apply("com.iamkurtgoz.sub.spotless")
                apply("com.iamkurtgoz.sub.ktlint")
                apply("com.iamkurtgoz.sub.detekt")
            }

            extensions.configure<LibraryExtension> {
                configureKotlinAndroid(this)
                defaultConfig.targetSdk = libs.findVersion("targetSdk").get().toString().toInt()
                compileSdk = libs.findVersion("compileSdk").get().toString().toInt()
            }
            dependencies {
                // Serialization
                implementation(libs.findLibrary("kotlinx.serialization.json").get())

                // Lifecycle
                implementation(libs.findLibrary("androidx-lifecycle-runtime").get())

                // Test
                androidTestImplementation(kotlin("test"))
                testImplementation(kotlin("test"))
                testImplementation(libs.findLibrary("junit4").get())
                androidTestImplementation(libs.findLibrary("junit4").get())
                androidTestImplementation(libs.findLibrary("androidx.test.ext").get())
            }
        }
    }
}
